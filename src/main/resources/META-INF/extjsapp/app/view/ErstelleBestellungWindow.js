Ext.define('MyApp.view.ErstelleBestellungWindow', {
	extend : 'Ext.window.Window',
	id : 'ErstelleBestellungWindow',
	frame : true,
	width : 770,
	layout : {
		align : 'stretch',
		type : 'vbox'
	},
	closeAction : 'hide',
	title : 'Erstelle Bestellung',

	initComponent : function() {
		var me = this;

		Ext.applyIf(me, {
			dockedItems : [{
				xtype : 'form',
				itemid : 'form',
				id : 'ErstelleBestellungForm',
				dock : 'top',
				layout : {
					align : 'stretch',
					type : 'anchor'
				},
				bodyPadding : 10,
				items : [{
							itemid : 'orderNumber',
							xtype : 'numberfield',
							// anchor: '100%',
							fieldLabel : 'Bestellnr',
							allowBlank : false,
							name : 'orderNumber',
							valueField : 'orderNumber',
							listeners : {
								change : this.onOrderNumberChange
							}
						}, {
							xtype : 'combobox',
							// anchor:'100%',
							name : 'customer',
							itemid : 'customer',
							fieldLabel : 'Kunde',
							displayField : 'shortName',
							valueField : 'id',
							// allowBlank: false,
							enableRegEx : true,
							forceSelection : true,
							queryMode : 'local',
							store : 'KundeDataStore',
							tpl : Ext
									.create(
											'Ext.XTemplate',
											'<tpl for=".">',
											'<div class="x-boundlist-item">{id} - {shortName}</div>',
											'</tpl>'),
							displayTpl : Ext.create('Ext.XTemplate',
									'<tpl for=".">', '{id} - {shortName}',
									'</tpl>')
						}, {
							xtype : 'BestellpositionGrid',
							id : 'BestellpositionGrid',
							store : 'BestellpositionDataStore',
							listeners : {
								create : function(form, data) {
									store.insert(0, data);
								},
								selectionchange : this.onSelectionchange
							}
						}],

				buttons : [{

							text : 'Speichern',
							formBind : true, // only enabled once the form is
							// valid
							disabled : true,
							id : 'SubmitBestellungButton',
							handler : this.onSave
						}]
			}]
		});

		me.callParent(arguments);
	},
	onOrderNumberChange : function(form, data) {
		var store = Ext.data.StoreManager.lookup('BestellpositionDataStore');
		console.log(data);
		Ext.data.StoreManager.lookup('BestellpositionDataStore').filters
		store.filters.removeAll();
		store.filter("orderNumber", data);
	},
	onSelectionchange : function(view, selections, options) {
		// TODO: l�schen und Hinzuf�gen Button der Bps de/aktivieren
		if (selections[0] != null) {
			var orderNumber = this.up('numberfield[orderNumber]');
			var deleteButton = this.down('button[itemid=delete]');
			var addButton = this.down('button[itemid=add]');
			if (selections[0].data.status == 'ORDERED') {
				addButton.setDisabled(false);
				deleteButton.setDisabled(false);
			} else {
				deleteButton.setDisabled(true);
			}
		}
	},
	onSave : function(btn) {
		form = Ext.getCmp('ErstelleBestellungForm').getForm();
		orderNumber = form.getValues().orderNumber;
		customerId = form.getValues().customer;
		store = Ext.data.StoreMgr.lookup('BestellpositionDataStore');
		grid = Ext.getCmp('newOrderBpg');
		if (orderNumber == 0 || orderNumber == "") {
			Ext.MessageBox.show({
						title : 'Bestellnummer leer',
						msg : 'Bitte eine Bestellnummer eingeben',
						icon : Ext.MessageBox.ERROR,
						buttons : Ext.Msg.OK
					});
		} else if (customerId == 0 || customerId == "") {
			Ext.MessageBox.show({
						title : 'Kundenfeld leer',
						msg : 'Bitte eine Kunden auswaehlen',
						icon : Ext.MessageBox.ERROR,
						buttons : Ext.Msg.OK
					});
		} else {
			store.sync();
		}
	}

});