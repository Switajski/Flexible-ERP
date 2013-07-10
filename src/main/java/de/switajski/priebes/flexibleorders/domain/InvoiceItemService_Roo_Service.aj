// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.switajski.priebes.flexibleorders.domain;

import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.InvoiceItemService;
import java.util.List;

privileged aspect InvoiceItemService_Roo_Service {
    
    public abstract long InvoiceItemService.countAllInvoiceItems();    
    public abstract void InvoiceItemService.deleteInvoiceItem(InvoiceItem invoiceItem);    
    public abstract InvoiceItem InvoiceItemService.findInvoiceItem(Long id);    
    public abstract List<InvoiceItem> InvoiceItemService.findAllInvoiceItems();    
    public abstract List<InvoiceItem> InvoiceItemService.findInvoiceItemEntries(int firstResult, int maxResults);    
    public abstract void InvoiceItemService.saveInvoiceItem(InvoiceItem invoiceItem);    
    public abstract InvoiceItem InvoiceItemService.updateInvoiceItem(InvoiceItem invoiceItem);    
}
