// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.switajski.priebes.flexibleorders.domain;

import de.switajski.priebes.flexibleorders.domain.InvoiceItemDataOnDemand;
import de.switajski.priebes.flexibleorders.domain.InvoiceItemIntegrationTest;
import de.switajski.priebes.flexibleorders.repository.InvoiceItemRepository;
import de.switajski.priebes.flexibleorders.service.InvoiceItemService;

import java.util.Iterator;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect InvoiceItemIntegrationTest_Roo_IntegrationTest {
    
    declare @type: InvoiceItemIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: InvoiceItemIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: InvoiceItemIntegrationTest: @Transactional;
    
    @Autowired
    InvoiceItemDataOnDemand InvoiceItemIntegrationTest.dod;
    
    @Autowired
    InvoiceItemService InvoiceItemIntegrationTest.invoiceItemService;
    
    @Autowired
    InvoiceItemRepository InvoiceItemIntegrationTest.invoiceItemRepository;
    
    @Test
    public void InvoiceItemIntegrationTest.testCountAllInvoiceItems() {
        Assert.assertNotNull("Data on demand for 'InvoiceItem' failed to initialize correctly", dod.getRandomInvoiceItem());
        long count = invoiceItemService.countAllInvoiceItems();
        Assert.assertTrue("Counter for 'InvoiceItem' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void InvoiceItemIntegrationTest.testFindInvoiceItem() {
        InvoiceItem obj = dod.getRandomInvoiceItem();
        Assert.assertNotNull("Data on demand for 'InvoiceItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'InvoiceItem' failed to provide an identifier", id);
        obj = invoiceItemService.findInvoiceItem(id);
        Assert.assertNotNull("Find method for 'InvoiceItem' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'InvoiceItem' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void InvoiceItemIntegrationTest.testFindAllInvoiceItems() {
        Assert.assertNotNull("Data on demand for 'InvoiceItem' failed to initialize correctly", dod.getRandomInvoiceItem());
        long count = invoiceItemService.countAllInvoiceItems();
        Assert.assertTrue("Too expensive to perform a find all test for 'InvoiceItem', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<InvoiceItem> result = invoiceItemService.findAllInvoiceItems();
        Assert.assertNotNull("Find all method for 'InvoiceItem' illegally returned null", result);
        Assert.assertTrue("Find all method for 'InvoiceItem' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void InvoiceItemIntegrationTest.testFindInvoiceItemEntries() {
        Assert.assertNotNull("Data on demand for 'InvoiceItem' failed to initialize correctly", dod.getRandomInvoiceItem());
        long count = invoiceItemService.countAllInvoiceItems();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<InvoiceItem> result = invoiceItemService.findInvoiceItemEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'InvoiceItem' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'InvoiceItem' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void InvoiceItemIntegrationTest.testFlush() {
        InvoiceItem obj = dod.getRandomInvoiceItem();
        Assert.assertNotNull("Data on demand for 'InvoiceItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'InvoiceItem' failed to provide an identifier", id);
        obj = invoiceItemService.findInvoiceItem(id);
        Assert.assertNotNull("Find method for 'InvoiceItem' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyInvoiceItem(obj);
        Integer currentVersion = obj.getVersion();
        invoiceItemRepository.flush();
        Assert.assertTrue("Version for 'InvoiceItem' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void InvoiceItemIntegrationTest.testUpdateInvoiceItemUpdate() {
        InvoiceItem obj = dod.getRandomInvoiceItem();
        Assert.assertNotNull("Data on demand for 'InvoiceItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'InvoiceItem' failed to provide an identifier", id);
        obj = invoiceItemService.findInvoiceItem(id);
        boolean modified =  dod.modifyInvoiceItem(obj);
        Integer currentVersion = obj.getVersion();
        InvoiceItem merged = (InvoiceItem)invoiceItemService.updateInvoiceItem(obj);
        invoiceItemRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'InvoiceItem' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void InvoiceItemIntegrationTest.testSaveInvoiceItem() {
        Assert.assertNotNull("Data on demand for 'InvoiceItem' failed to initialize correctly", dod.getRandomInvoiceItem());
        InvoiceItem obj = dod.getNewTransientInvoiceItem(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'InvoiceItem' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'InvoiceItem' identifier to be null", obj.getId());
        try {
            invoiceItemService.saveInvoiceItem(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        invoiceItemRepository.flush();
        Assert.assertNotNull("Expected 'InvoiceItem' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void InvoiceItemIntegrationTest.testDeleteInvoiceItem() {
        InvoiceItem obj = dod.getRandomInvoiceItem();
        Assert.assertNotNull("Data on demand for 'InvoiceItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'InvoiceItem' failed to provide an identifier", id);
        obj = invoiceItemService.findInvoiceItem(id);
        invoiceItemService.deleteInvoiceItem(obj);
        invoiceItemRepository.flush();
        Assert.assertNull("Failed to remove 'InvoiceItem' with identifier '" + id + "'", invoiceItemService.findInvoiceItem(id));
    }
    
}
