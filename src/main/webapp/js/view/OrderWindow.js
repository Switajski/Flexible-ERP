Ext.define('MyApp.view.OrderWindow', {
	extend : 'MyApp.view.TransitionWindow',
	title : 'Bestellung aufgeben',
	width : 700,
	id : 'OrderWindow',
	headerForm : {
		xtype : 'fieldset',
		// title : 'Kunde',
		flex : 1,
		items : [{
					xtype : 'displayfield',
					anchor : '100%',
					name : 'customerNumber',
					fieldLabel : 'Kundennr'
				}, {
					xtype : 'displayfield',
					anchor : '100%',
					name : 'lastName',
					fieldLabel : 'Nachname'
				}, {
					xtype : 'datefield',
					format : 'd/m/Y',
					allowBlank : true,
					fieldLabel : 'Bestelldatum',
					name : 'created'
				}, {
					itemid : 'newOrderNumber',
					xtype : 'ordernumbercombobox',
					fieldLabel : 'Bestellnr',
					listeners : {
						// change : this.onOrderNumberChange,
						specialkey : function(field, e) {
							if (e.getKey() == e.ENTER) {
								me.onOrderNumberChange(field, e);
								Ext.ComponentQuery
										.query('#ErstelleBestellungForm button[itemid=add]')[0]
										.focus();
							}
						}
					}
				}]
	},
	deliveryAddressForm : null,
	addressForm : null,
	bottomGrid : {
		xtype : 'BestellpositionGrid',
		getOrderNumber : function() {
			return Ext.ComponentQuery.query('combobox[itemid=newOrderNumber]')[0].rawValue;
		},
		plugins : [Ext.create('Ext.grid.plugin.CellEditing', {
					clicksToEdit : 1
				})],
		dock : 'bottom',
		id : 'CreateOrderGrid',
		flex : 1,
		store : 'CreateOrderDataStore',
		// store : 'InvoiceItemDataStore',
		title : "Bestellpositionen",
		features : null,
		columns : [{
			xtype : 'gridcolumn',
			dataIndex : 'product',
			text : 'Artikel',
			width : 250,
			renderer : function(value, metaData, record, row, col, store,
					gridView) {
				console.log(value);
				return (value + ' - ' + record.data.productName);
			},
			editor : {
				id : 'ArtikelComboBox',
				xtype : 'combobox',
				displayField : 'name',
				valueField : 'productNumber',
				enableRegEx : true,
				allowBlank : false,
				forceSelection : true,
				queryMode : 'local',
				store : 'ArtikelDataStore',
				tpl : Ext
						.create(
								'Ext.XTemplate',
								'<tpl for="."><div class="x-boundlist-item" >{productNumber} - {name}</div></tpl>'),
				displayTpl : Ext.create('Ext.XTemplate', '<tpl for=".">',
						'{productNumber} - {name}', '</tpl>'),
				listeners : {
					'blur' : function(xObject, state, eOpts) {
						rowPos = Ext.getCmp('CreateOrderGrid')
								.getSelectionModel().getCurrentPosition().row;
						data = Ext.getStore('ArtikelDataStore').query(
								'productNumber', xObject.value).getAt(0).data;

						createOrderStore = Ext.data.StoreMgr
								.lookup('CreateOrderDataStore');
						record = createOrderStore.getAt(rowPos);
						record.set('priceNet', data.recommendedPriceNet.value);
						record.set('productName', data.name);

					}
				}
			}
		}, {
			xtype : 'gridcolumn',
			dataIndex : 'productName',
			width : 150,
			text : 'Artikelname',
			filter : {
				type : 'string'
				// , disabled: true
			},
			editor : {
				xtype : 'textfield',
				allowBlank : false
			}
		}, {
			xtype : 'gridcolumn',
			dataIndex : 'orderNumber',
			width : 100,
			text : 'Bestellung',
			filter : {
				type : 'string'
				// , disabled: true
			}
		}, {
			xtype : 'gridcolumn',
			dataIndex : 'quantity',
			width : 75,
			text : 'Menge',
			value : 1,
			minValue : 1,
			editor : {
				xtype : 'numberfield',
				allowBlank : false,
				minValue : 1
			}
		}, {
			xtype : 'numbercolumn',
			dataIndex : 'priceNet',
			width : 100,
			text : 'Preis Netto',
			renderer : Ext.util.Format.euMoney,
			editor : {
				xtype : 'numberfield',
				allowBlank : false
			}
		}]
	},
	onSave : function(button, event, option) {
		MyApp.getApplication().getController('OrderController').order(button,
				event, option);
	}
});