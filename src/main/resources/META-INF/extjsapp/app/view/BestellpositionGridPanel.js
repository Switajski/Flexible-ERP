var filters = {
	ftype : 'filters',
	// encode and local configuration options defined previously for easier
	// reuse
	encode : true, // json encode the filter query
	local : false, // defaults to false (remote filtering)

	// Filters are most naturally placed in the column definition, but can also
	// be
	// added here.
	filters : [{
				type : 'string',
				dataIndex : 'visible'
			}]
};

Ext.define('MyApp.view.BestellpositionGridPanel', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.BestellpositionGrid',
	// width: 250,
	// height: 300,
	title : "Bestellpositionen",
	requires : ['MyApp.model.ArtikelData',
			'MyApp.store.BestellpositionDataStore',
			'Ext.grid.plugin.CellEditing', 'Ext.ux.grid.FiltersFeature',
			'Ext.form.field.Text', 'Ext.toolbar.TextItem'],
	// store: 'BestellpositionDataStore',
	initComponent : function() {
		var me = this;
		this.editing = Ext.create('Ext.grid.plugin.CellEditing');

		Ext.applyIf(me, {
			plugins : [this.editing],
			tbar : [{
						itemid: 'add',
						dock : 'top',
						icon : 'images/add.png',
						text : 'hinzuf&uuml;gen',
						scope : this,
						handler : this.onAddClick
					}, {
						itemid : 'delete',
						icon : 'images/delete.png',
						text : 'l&ouml;schen',
						scope : this,
						handler : this.onDeleteClick
					}],
			dockedItems : [{
						xtype : 'toolbar',
						dock : 'bottom',
						text : 'sync',
						scope : this
					}],
			
			features : [filters],
			columns : [{
				xtype : 'gridcolumn',
				dataIndex : 'product',
				text : 'Artikel',
				width : 200,
				// name: 'productNumber',
				editor : {
					id : 'ArtikelComboBox',
					xtype : 'combobox',
					displayField: 'name',
					valueField : 'productNumber',
					enableRegEx : true,
					allowBlank : false,
					forceSelection : true,
					queryMode : 'local',
					store : 'ArtikelDataStore',
					tpl : Ext
							.create(
									'Ext.XTemplate',
									'<tpl for=".">',
									'<div class="x-boundlist-item">{productNumber} - {name}</div>',
									'</tpl>'),
					displayTpl : Ext.create('Ext.XTemplate', '<tpl for=".">',
							'{productNumber} - {name}', '</tpl>')
				}
			}, {
				xtype : 'gridcolumn',
				dataIndex : 'orderNumber',
				width : 75,
				text : 'Bestellung',
				filter : {
					type : 'string'
					// , disabled: true
				}
			}, {
				xtype : 'gridcolumn',
				dataIndex : 'invoiceNumber',
				width : 75,
				text : 'Rechnung'
			}, {
				xtype : 'gridcolumn',
				dataIndex : 'orderConfirmationNumber',
				width : 75,
				text : 'AB'
			}, {
				xtype : 'gridcolumn',
				dataIndex : 'quantity',
				width : 50,
				text : 'Menge',
				editor : {
					xtype : 'numberfield',
					allowBlank : false,
					minValue : 1
				}

			}, {
				xtype : 'numbercolumn',
				dataIndex : 'priceNet',
				width : 75,
				text : 'Preis Netto',
				renderer : Ext.util.Format.euMoney,
				editor : {
					xtype : 'numberfield',
					allowBlank : true
				}
			}, {
				xtype : 'gridcolumn',
				dataIndex : 'status',
				width : 75,
				text : 'Status'
			}, {
				xtype : 'gridcolumn',
				dataIndex : 'expectedDelivery',
				text : 'Geplante Auslieferung',
				width : 110,
				format : 'd/m/Y',
				editor : {
					xtype : 'datefield',
					format : 'd/m/Y',
					allowBlank : true,
					minValue : Ext.Date.format(new Date(), 'd/m/Y'),
					minText : 'Datum liegt in der Vergangenheit'
				}
			}]
		});
		me.callParent(arguments);

	},

	onSync : function() {
		this.store.sync();
	},

	onAddClick : function() {
		bestellnr = Ext.ComponentQuery.query('form[itemid="form"]')[0]
				.getForm().getValues().orderNumber;
		customer = Ext.ComponentQuery.query('form[itemid="form"]')[0].getForm()
				.getValues().customer;
		console.log('hierher!');
		if (bestellnr == null || bestellnr == 0 || bestellnr == "") {
			Ext.MessageBox.show({
						title : 'Bestellnummer leer',
						msg : 'Bitte eine Bestellnummer eingeben',
						icon : Ext.MessageBox.ERROR,
						buttons : Ext.Msg.OK
					});
		} else if (customer == null || customer == 0 || customer == "") {
			Ext.MessageBox.show({
						title : 'Keinen Kunden ausgewaehlt',
						msg : 'Bitte einen Kunden auswaehlen',
						icon : Ext.MessageBox.ERROR,
						buttons : Ext.Msg.OK
					});
		} else {
			var rec = new MyApp.model.BestellpositionData({
						status : 'ORDERED'
					}), edit = this.editing;
			rec.data.orderNumber = bestellnr;
			rec.data.customer = customer;
			this.store.insert(0, rec);
			edit.startEditByPosition({
						row : 0,
						column : 0
					});
		}
	},
	onDeleteClick : function() {
		var selection = this.getView().getSelectionModel().getSelection()[0];
		if (selection) {
			this.store.remove(selection);
		}
	}
	
});