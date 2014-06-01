// Global Exception Handling
Ext.Ajax.on('requestexception', function(conn, response, options) {
			if (response.status === 400) {
				Ext.MessageBox.alert('Eingabefehler', response.responseText);
			} else {
				Ext.MessageBox.alert('Server meldet Fehler',
						response.responseText);
			}
		});

Ext.override(Ext.data.JsonWriter, {
			encode : false,
			writeAllFields : true,
			listful : true,
			constructor : function(config) {
				this.callParent(this, config);
				return this;
			},
			render : function(params, baseParams, data) {
				params.jsonData = data;
			}
		});

Ext.define('MyApp.controller.MyController', {
	debug : true,
	extend : 'Ext.app.Controller',

	id : 'MyController',
	models : ['BestellungData', 'ItemData', 'KundeData'],
	stores : ['BestellungDataStore', 'ItemDataStore',
			'KundeDataStore', 'InvoiceItemDataStore', 'ShippingItemDataStore',
			'ArchiveItemDataStore', 'OrderNumberDataStore',
			'DeliveryNotesItemDataStore', 'InvoiceNumberDataStore',
			'CreateOrderDataStore', 'CreateDeliveryNotesItemDataStore',
			'CreateInvoiceItemDataStore', 'DeliveryNotesItemDataStore',
			'CreateConfirmationReportItemDataStore'],
	views : ['MainPanel', 'BestellungWindow', 'CreateCustomerWindow',
			'BestellpositionGridPanel',
			'ConfirmWindow', 'DeliverWindow',
			'OrderNumberComboBox', 'InvoiceNumberComboBox',
			'OrderWindow', 'InvoiceWindow', 'DeliveryNotesItemGridPanel'],
	// TODO: Registrieren und Initialisiseren von Views an einer Stelle
	// implementieren

	activeBestellnr : 0,
	activeBestellpositionId : 0,
	bestellungDataStore : null,

	activeCustomer : 0,

	init : function(application) {
		this.control({
					'#mainCustomerComboBox' : {
						change : this.onCustomerChange
					},
					'#ErstelleBestellungButton' : {
						click : this.onOrder
					},
					'#DeleteBpButton' : {
						click : this.deleteBpDialog
					},
					'#DeleteBestellungButton' : {
						click : this.deleteBestellungDialog
					},
					'#SubmitBpButton' : {
						click : this.addBp
					},
					'#AbBestellungButton' : {
						click : this.bestaetigeAuftragDialog
					},
					'#BestellungPdf' : {
						click : this.showBestellundPdf
					},
					'#AbPdf' : {
						click : this.showAbPdf
					},
					'#RechnungPdf' : {
						click : this.showRechnungPdf
					},
					'#CreateShippingCostsButton' : {
						click : this.createShippingCosts
					},
					'#AddShippingCostsButton' : {
						click : this.addShippingCosts
					}
				});
		this.getStore('ItemDataStore').filter('status', 'ordered');
		this.getStore('ShippingItemDataStore').filter('status', 'confirmed');
		this.getStore('DeliveryNotesItemDataStore').filter('status', 'shipped');
		this.getStore('InvoiceItemDataStore').filter('status', 'invoiced');
		this.getStore('ArchiveItemDataStore').filter('status', 'completed');
	},

	onSelectionchange : function(view, selections, options) {
		if (view.getStore().storeId != "BestellungDataStore")
			return;
		if (selections.length == 0)
			return;
	},
	getButtons : function() {
		var buttons = {
			createCustomer : Ext.getCmp('CreateCustomerButton'),
			deleteBp : Ext.getCmp('DeleteBpButton'),
			erstelleBpButton : Ext.getCmp('ErstelleBpButton'),
			deleteBestellung : Ext.getCmp('DeleteBestellungButton'),
			erstelleBestellungButton : Ext.getCmp('ErstelleBestellungButton'),
			abBestellungButton : Ext.getCmp('AbBestellungButton'),
			rechnungBestellungButton : Ext.getCmp('RechnungBestellungButton'),
			bezahltBestellungButton : Ext.getCmp('BezahltBestellungButton'),
			stornoBestellungButton : Ext.getCmp('StornoBestellungButton'),
			deleteBestellungButton : Ext.getCmp('DeleteBestellungButton'),
			bestellungPdfButton : Ext.getCmp('BestellungPdf'),
			abPdfButton : Ext.getCmp('AbPdf'),
			rechnungPdfButton : Ext.getCmp('RechnungPdf'),
			offeneBpPdfButton : Ext.getCmp('OffeneBpPdf')
		};
		return buttons;
	},

	deleteBpDialog : function(button, event, options) {
		var bestellung = this.getBpSelection();

		if (bestellung.getData().status == 'ORDERED')
			Ext.MessageBox.confirm('Best&aumltigen',
					'Bestellpostion sicher l&ouml;schen?', this.deleteBp);
		else
			Ext.MessageBox
					.alert('Hinweis',
							'Bestellposition schon best&auml;tigt. Nur noch Storno ist m&ouml;glich.');
	},

	deleteBestellungDialog : function(button, event, options) {
		var bestellung = this.getBestellungSelection();

		if (bestellung.getData().status == 'ORDERED')
			Ext.MessageBox.confirm('Best&aumltigen',
					'Bestellung sicher l&ouml;schen?', this.deleteBestellung);
		else
			Ext.MessageBox
					.alert('Hinweis',
							'Bestellung schon best&auml;tigt. Nur noch Storno ist m&ouml;glich.');
	},

	getBpSelection : function() {
		var bpGridPanel = Ext.getCmp('BestellpositionGridPanel');
		var selectionModel = bpGridPanel.getSelectionModel();
		var bp = selectionModel.getSelection()[0];
		return bp;
	},

	syncBpGrid : function(view, owner, options) {
		var bpDataStore = Ext.data.StoreManager
				.lookup('ItemDataStore');
	},

	showBestellundPdf : function(button, event, options) {
		var win = window.open('/leanorders/orders/' + this.activeBestellnr
						+ '.pdf', '_blank');
		win.focus();
	},
	showAbPdf : function(button, event, options) {
		var win = window.open('/leanorders/orderConfirmations/'
						+ this.activeBestellnr + '.pdf', '_blank');
		win.focus();
	},
	showRechnungPdf : function(button, event, options) {
		var win = window.open('/leanorders/invoices/' + this.activeBestellnr
						+ '.pdf', '_blank');
		win.focus();
	},
	sleep : function(milliseconds) {
		var start = new Date().getTime();
		for (var i = 0; i < 1e7; i++) {
			if ((new Date().getTime() - start) > milliseconds) {
				break;
			}
		}
	},

	deconfirm : function(event, ocnr, record) {
		store = Ext.getStore('ShippingItemDataStore');
		var request = Ext.Ajax.request({
					url : '/FlexibleOrders/transitions/cancelConfirmationReport',
					params : {
						confirmationNumber : ocnr
					},
					success : function(response) {
						store.remove(store.getGroups(ocnr).children);
					}
				});
	},

	withdraw : function(event, record) {
		if (event == "ok") {
			var request = Ext.Ajax.request({
						url : '/FlexibleOrders/transitions/cancelDeliveryNotes',
						params : {
							invoiceNumber : record.data.invoiceNumber
						},
						success : function(response) {
							store.remove(store.getGroups(ocnr).children);
						}
					});
		}
	},

	decomplete : function(event, record) {
		if (event == "ok") {

			var request = Ext.Ajax.request({
				url : '/FlexibleOrders/transitions/decomplete/json',
				params : {
					id : record.data.id,
					productNumber : record.data.product,
					orderConfirmationNumber : record.data.orderConfirmationNumber,
					invoiceNumber : record.data.invoiceNumber,
					accountNumber : record.data.accountNumber,
					quantity : record.data.quantity
				},
				success : function(response) {
					// TODO: make a responsive design
					var text = response.responseText;
				}
			});
			// TODO: DRY in Sync
			// Sync
			MyApp.getApplication().getController('MyController').sleep(500);
			var allGrids = Ext.ComponentQuery.query('PositionGrid');
			allGrids.forEach(function(grid) {
						grid.getStore().load();
					});
		}
	},

	complete : function(event, anr, record) {
		record.data.accountNumber = record.data.invoiceNumber
				.replace(/R/g, "Q");
		if (event == "ok") {

			var request = Ext.Ajax.request({
						url : '/FlexibleOrders/transitions/complete/json',
						params : {
							id : record.data.id,
							productNumber : record.data.product,
							quantity : record.data.quantity,
							invoiceNumber : record.data.invoiceNumber,
							accountNumber : record.data.accountNumber
						},
						jsonData : {
							invoiceNumber : record.data.invoiceNumber
						},
						success : function(response) {
							var text = response.responseText;
						}
					});

			// Sync
			MyApp.getApplication().getController('MyController').sleep(500);
			var allGrids = Ext.ComponentQuery.query('PositionGrid');
			allGrids.forEach(function(grid) {
						grid.getStore().load();
					});
		}
	},

	onCustomerChange : function(field, newValue, oldValue, eOpts) {
		var stores = new Array();
		stores[0] = Ext.data.StoreManager.lookup('ItemDataStore');
		stores[1] = Ext.data.StoreManager.lookup('ShippingItemDataStore');
		stores[2] = Ext.data.StoreManager.lookup('DeliveryNotesItemDataStore');
		stores[3] = Ext.data.StoreManager.lookup('InvoiceItemDataStore');
		stores[4] = Ext.data.StoreManager.lookup('ArchiveItemDataStore');

		stores.forEach(function(store) {
					found = false;
					store.filters.items.forEach(function(filter) {
								if (filter.property == 'customer') {
									filter.value = newValue;
									found = true;
									store.load();
								}
							});
					if (!found) {
						store.filter("customer", newValue);
						// store.load();
					}
				});
	},

	// TODO: rename to onDeliver
	/**
	 * 
	 * @param {}
	 *            function only succeds if event is equals "ok". This variable
	 *            if for previous confirmation messages.
	 * @param {}
	 *            record selected shipping item from a grid
	 */
	deliver : function(event, record) {
		deliveryNotesNumber = record.data.documentNumber.replace(/AB/g, "L");

		record.data.deliveryNotesNumber = record.data.documentNumber;
		var createDeliveryNotesStore = MyApp.getApplication()
				.getStore('CreateDeliveryNotesItemDataStore');
		createDeliveryNotesStore.filter([{
					property : "customer",
					value : record.data.customer
				}, {
					property : "status",
					value : "confirmed"
				}]);

		var deliverWindow = Ext.create('MyApp.view.DeliverWindow', {
					id : "DeliverWindow",
					onSave : function() {
						MyApp
								.getApplication()
								.getController('MyController')
								.deliver2("ok", kunde, createDeliveryNotesStore);
					}
				});
		kunde = Ext.getStore('KundeDataStore').findRecord("id",
				record.data.customer);
		kundeId = kunde.data.id;
		email = kunde.data.email;

		deliverWindow.show();
		deliverWindow.down('form').getForm().setValues({
					name1 : kunde.data.name1,
					name2 : kunde.data.name2,
					city : kunde.data.city,
					country : kunde.data.country,
					email : kunde.data.email,
					firstName : kunde.data.firstName,
					id : kunde.data.id,
					lastName : kunde.data.lastName,
					phone : kunde.data.phone,
					postalCode : kunde.data.postalCode,
					customerNumber : kunde.data.customerNumber,
					street : kunde.data.street
				});
		// somehow the id is deleted onShow
		Ext.getCmp('deliveryNotesNumber').setValue(deliveryNotesNumber);
		Ext.getStore('KundeDataStore').findRecord("email", email).data.id = kundeId;
	},

	onOrder : function(button, event, option) {
		// check customer is chosen
		var customerId = Ext.getCmp('mainCustomerComboBox').getValue();
		if (customerId == 0 || customerId == "" || customerId == null) {
			Ext.MessageBox.show({
						title : 'Kundenfeld leer',
						msg : 'Bitte Kunden ausw&auml;hlen',
						icon : Ext.MessageBox.ERROR,
						buttons : Ext.Msg.OK
					});
			return;
		}

		var customer = MyApp.getApplication().getStore('KundeDataStore')
				.getById(customerId);

		var orderWindow = Ext.create('MyApp.view.OrderWindow', {
			id : "OrderWindow",
			record : customer,
			onShow : function() {
				this.down('form').getForm().loadRecord(customer);
			}
				/*
				 * onSave : function() {
				 * MyApp.getApplication().getController('MyController')
				 * .deliver2("ok", kunde, createInvoiceStore); },
				 */
			});
		orderWindow.show();
		orderWindow.focus();
	},
	
	/**
	 * 
	 * @param {}
	 *            event
	 * @param {}
	 *            record
	 * @param {}
	 *            createInvoiceStore
	 */
	deliver2 : function(event, record, createDeliveryNotesStore) {
		var form = Ext.getCmp('DeliverWindow').down('form').getForm();
		if (event == "ok") {

			var request = Ext.Ajax.request({
				url : '/FlexibleOrders/transitions/deliver/json',
				// headers: { 'Content-Type': 'application/json' },
				jsonData : {
					orderConfirmationNumber : form.getValues().confirmationNumber,
					customerId : form.getValues().id,
					name1 : form.getValues().name1,
					name2 : form.getValues().name2,
					street : form.getValues().street,
					postalCode : form.getValues().postalCode,
					city : form.getValues().city,
					country : form.getValues().country,
					deliveryNotesNumber : form.getValues().deliveryNotesNumber,
					shipment : form.getValues().shipment,
					packageNumber : form.getValues().packageNumber,
					trackNumber : form.getValues().trackNumber,
					items : Ext.pluck(createDeliveryNotesStore.data.items,
							'data')
				},
				success : function(response) {
					var text = response.responseText;
					// Sync
					MyApp.getApplication().getController('MyController')
							.sleep(500);
					var allGrids = Ext.ComponentQuery.query('PositionGrid');
					allGrids.forEach(function(grid) {
								grid.getStore().load();
							});
					Ext.getCmp("DeliverWindow").close();
				}
			});
		}
	},

	/**
	 * called by OrderWindow
	 * 
	 * @param {}
	 *            button
	 * @param {}
	 *            event
	 * @param {}
	 *            option
	 */
	order : function(button, event, option) {
		var form = Ext.getCmp('OrderWindow').down('form').getForm();
		var record = form.getRecord();

		var request = Ext.Ajax.request({
			url : '/FlexibleOrders/transitions/order',
			jsonData : {
				orderNumber : form.getValues().order,
				customerId : record.data.id,
				name1 : record.data.name1,
				name2 : record.data.name2,
				street : record.data.street,
				postalCode : record.data.postalCode,
				city : record.data.city,
				country : record.data.country,
				invoiceNumber : form.getValues().invoiceNumber,
				packageNumber : form.getValues().packageNumber,
				trackNumber : form.getValues().trackNumber,
				items : Ext.pluck(
						Ext.getCmp('CreateOrderGrid').getStore().data.items,
						'data')
			},
			success : function(response) {
				var text = response.responseText;
				// Sync
				MyApp.getApplication().getController('MyController').sleep(500);
				var allGrids = Ext.ComponentQuery.query('PositionGrid');
				allGrids.forEach(function(grid) {
							grid.getStore().load();
						});
				Ext.getCmp('CreateOrderGrid').getStore().removeAll();
				Ext.getCmp("OrderWindow").close();
			}
		});
	},

	invoice : function(event, record) {
		deliveryNotesNumber = record.data.deliveryNotesNumber
				.replace(/L/g, "R");

		record.data.deliveryNotesNumber = record.data.deliveryNotesNumber;
		var createInvoiceStore = MyApp.getApplication()
				.getStore('CreateInvoiceItemDataStore');
		createInvoiceStore.filter([{
					property : "customer",
					value : record.data.customer
				}, {
					property : "status",
					value : "shipped"
				}]);

		var invoiceWindow = Ext.create('MyApp.view.InvoiceWindow', {
					id : "InvoiceWindow",
					onSave : function() {
						MyApp.getApplication().getController('MyController')
								.invoice2("ok", kunde, createInvoiceStore);
					}
				});
		kunde = Ext.getStore('KundeDataStore').findRecord("id",
				record.data.customer);
		kundeId = kunde.data.id;
		email = kunde.data.email;

		invoiceWindow.show();
		invoiceWindow.down('form').getForm().setValues({
			name1 : kunde.data.name1,
			name2 : kunde.data.name2,
			city : kunde.data.city,
			country : kunde.data.country,
			email : kunde.data.email,
			firstName : kunde.data.firstName,
			id : kunde.data.id,
			lastName : kunde.data.lastName,
			phone : kunde.data.phone,
			postalCode : kunde.data.postalCode,
			customerNumber : kunde.data.customerNumber,
			street : kunde.data.street,
			paymentConditions : "Rechnungsbetrag ist zahlbar innerhalb von 30 Tagen. 3% Skonto bei Zahlung innerhalb von 8 Tagen."
		});
		// somehow the id is deleted onShow
		Ext.getCmp('invoiceNumber').setValue(deliveryNotesNumber);
		Ext.getStore('KundeDataStore').findRecord("email", email).data.id = kundeId;
	},

	invoice2 : function(event, record, createInvoiceStore) {
		var form = Ext.getCmp('InvoiceWindow').down('form').getForm();
		if (event == "ok") {
			var request = Ext.Ajax.request({
						url : '/FlexibleOrders/transitions/invoice/json',
						jsonData : {
							customerId : form.getValues().id,
							name1 : form.getValues().name1,
							name2 : form.getValues().name2,
							street : form.getValues().street,
							postalCode : form.getValues().postalCode,
							city : form.getValues().city,
							country : form.getValues().country,
							invoiceNumber : form.getValues().invoiceNumber,
							paymentConditions : form.getValues().paymentConditons,
							items : Ext.pluck(createInvoiceStore.data.items,
									'data')
						},
						success : function(response) {
							var text = response.responseText;
							// Sync
							MyApp.getApplication()
									.getController('MyController').sleep(500);
							var allGrids = Ext.ComponentQuery
									.query('PositionGrid');
							allGrids.forEach(function(grid) {
										grid.getStore().load();
									});
							Ext.getCmp("InvoiceWindow").close();
						}
					});
		}
	},
	
	deleteReport : function(varDocumentNumber) {
		var request = Ext.Ajax.request({
			url : '/FlexibleOrders/transitions/deleteReport',
			params : {
				documentNumber : varDocumentNumber
			},
			success : function(response) {
				var text = response.responseText;
				// Sync
				MyApp.getApplication().getController('MyController').sleep(500);
			}
		});
	}

});
