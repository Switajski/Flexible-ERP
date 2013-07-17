// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.switajski.priebes.flexibleorders.service;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.service.ArchiveItemService;
import java.util.List;

privileged aspect ArchiveItemService_Roo_Service {
    
    public abstract long ArchiveItemService.countAllArchiveItems();    
    public abstract void ArchiveItemService.deleteArchiveItem(ArchiveItem archiveItem);    
    public abstract ArchiveItem ArchiveItemService.findArchiveItem(Long id);    
    public abstract List<ArchiveItem> ArchiveItemService.findAllArchiveItems();    
    public abstract List<ArchiveItem> ArchiveItemService.findArchiveItemEntries(int firstResult, int maxResults);    
    public abstract void ArchiveItemService.saveArchiveItem(ArchiveItem archiveItem);    
    public abstract ArchiveItem ArchiveItemService.updateArchiveItem(ArchiveItem archiveItem);    
}
