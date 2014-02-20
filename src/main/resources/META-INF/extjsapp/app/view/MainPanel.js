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

Ext.define('MyApp.view.MainPanel', {
	extend : 'Ext.panel.Panel',
	frame : false,
	//height : 850,
	//width : 1000,
	layout : {
		align : 'stretch',
		type : 'vbox'
	},
	bodyPadding: 7,
	title : 'Stand der Bestellungen',
	requires : ['MyApp.store.BestellpositionDataStore'],
	dockedItems : [{
		xtype : 'toolbar',
		dock : 'top',
		items : [{
			id : 'ErstelleBestellungButton',
			icon : '/FlexibleOrders/images/add.png',
			text : 'erstelle Bestellung',
			scope : this
		}, {
			id : 'CreateCustomerButton',
			icon : '/FlexibleOrders/images/add.png',
			text : 'erstelle Kunden',
			scope : this
		}/*,{
			xtype : 'button',
			text : 'Berichte',
			icon : '/FlexibleOrders/images/pdf_button.png',
			menu : {
				xtype : 'menu',
				minWidth : 120,
				items : [{

							id : 'showOrderedButton',
							icon : '/FlexibleOrders/images/add.png',
							text : 'nicht best&auml;tigte Bestellpositionen anzeigen',
							scope : this
						}, {

							id : 'showConfirmedButton',
							icon : '/FlexibleOrders/images/add.png',
							text : 'nicht gelieferte Auftragspositionen anzeigen',
							scope : this
						}, {

							id : 'showShippedButton',
							icon : '/FlexibleOrders/images/add.png',
							text : 'nicht bezahlte Lieferungpositionen anzeigen',
							scope : this
						}, {

							id : 'showCompletedButton',
							icon : '/FlexibleOrders/images/add.png',
							text : 'bezahlte/archivierte Positionen anzeigen',
							scope : this
						}, {
							xtype : 'menuseparator'
						}]
			}
		}, {
			id : 'AbBestellungButton',
			text : 'Auftrag best&auml;tigen',
			icon : '/FlexibleOrders/images/new_ab.png',
			tooltip : 'Auftrag bestaetigen. Damit kommen die Bestellpositionen unter offene Posten',
			scope : this
		}/*, {
			id : 'RechnungBestellungButton',
			text : 'Rechnung erstellen',
			icon : '/FlexibleOrders/images/new_rechnung.png',
			tooltip : 'Rechnung in Pdf erstellen und aus den offenen Posten herausnehmen.',
			schope : this
		}, {
			id : 'BezahltBestellungButton',
			text : 'Als Bezahlt markieren',
			icon : '/FlexibleOrders/images/bezahlt.png',
			tooltip : 'Bestellung als bezahlt markieren',
			schope : this
		}*//*, {
			id : 'StornoBestellungButton',
			icon : 'images/delete_task.png',
			text : 'stornieren',
			tooltip : 'Die Bestellung bleibt in der Datenbank, wird allerdings als "Storniert" markiert',
			scope : this
		}, {
			id : 'DeleteBestellungButton',
			icon : 'images/delete.png',
			text : 'l&ouml;schen',
			tooltip : 'Bestellung wird engdg&uuml;ltig aus der Datenbank gel&ouml;scht.',
			scope : this
		}*/]

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
							xtype : 'OrderItemGrid',
							store : 'BestellpositionDataStore',
							customurl : '/FlexibleOrders/customers/json/getItems'
						}, {
							xtype : 'ShippingItemGrid',
							store : 'ShippingItemDataStore'
						}, {
							xtype : 'InvoiceItemGrid',
							store : 'InvoiceItemDataStore'
						}, {
							xtype : 'ArchiveItemGrid',
							store : 'ArchiveItemDataStore'
						}

				]
			}]
				// { xtype:'bestellunggridpanel' },

		});
		me.callParent(arguments);
	}
});