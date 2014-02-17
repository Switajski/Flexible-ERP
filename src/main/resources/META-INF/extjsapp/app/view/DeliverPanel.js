/*
 * File: app/view/MainPanel.js
 * 
 * This file was generated by Sencha Architect version 2.2.2.
 * http://www.sencha.com/products/architect/
 * 
 * This file requires use of the Ext JS 4.2.x library, under independent
 * license. License of Sencha Architect does not include license for Ext JS
 * 4.2.x. For more details see http://www.sencha.com/license or contact
 * license@sencha.com.
 * 
 * This file will be auto-generated each and everytime you save your project.
 * 
 * Do NOT hand edit this file.
 */

//TODO delete this - not used anymore
Ext.define('MyApp.view.DeliverPanel', {
	extend : 'Ext.panel.Panel',
	frame : false,
	//height : 850,
	//width : 1000,
	layout : {
		align : 'stretch',
		type : 'vbox'
	},
	title : 'Auftragspositionen liefern',
	requires : ['MyApp.store.ShippingItemDataStore'],
	dockedItems : [{
		xtype : 'toolbar',
		dock : 'top',
		items : [{
			id : 'AddShippingCostsButton',
			icon : '/FlexibleOrders/images/add.png',
			text : 'Fuege Versandkosten hinzu',
			scope : this
		},
		{
			id : 'CreateShippingCostsButton',
			icon : '/FlexibleOrders/images/add.png',
			text : 'Erstelle Versandkosten',
			scope : this
		}]

	}],

	initComponent : function() {
		var me = this;

		Ext.applyIf(me, {
			items : [{
				xtype : 'fieldcontainer',
				items : [{
							xtype : 'customercombobox',
							id : 'mainCustomerComboBox',
							fieldLabel: 'Kunde'
						}, {
							xtype : 'ShippingItemGrid',
							store : 'ShippingItemDataStore',
							customurl : '/FlexibleOrders/customers/json/getItems'
							/*extraParams : {
								customer : 1,
								itemType : 'ordered'
							},*/
							
						}, {
							xtype : 'InvoiceItemGrid',
							title : 'Gelieferte Auftragspositionen (offene Rechnungspositionen)',
							store : 'InvoiceItemDataStore'
						}

				]
			}]
		});
		me.callParent(arguments);
	}
});