Ext.define('MyApp.view.ShippingItemGridPanel', {
	extend : 'MyApp.view.PositionGridPanel',
	alias : 'widget.ShippingItemGrid',
	title : "Zu liefernde Auftragspositionen",
	onActionClick : function(view, a, b, column, event, record, f) {
				console.log('ShippingItemGrid - customtransitionfunction');
				MyApp.getApplication().getController('MyController').deliver(
						"ok", record);

			},
	onPdfClick : function(view, a, b, column, event, record, f){
				var win = window.open('/FlexibleOrders/orderconfirmations/' + record.data.orderConfirmationNumber
						+ '.pdf', '_blank');
				win.focus();
			},
	onRemoveClick: function(view, a, b, column, event, record, f) {
				console.log('orderItemGrid - customtransitionfunction');
				// var secondStore =
				// Ext.ComponentQuery.query('grid[itemid=secondGrid]')[0].getStore();
				var ocnr = this.getStore().data.items[0].data.orderNumber;
				MyApp.getApplication().getController('MyController').deconfirm(
						"ok", ocnr, record);

			}
});