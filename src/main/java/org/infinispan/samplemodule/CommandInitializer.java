package org.infinispan.samplemodule;

import org.infinispan.Cache;
import org.infinispan.commands.ReplicableCommand;
import org.infinispan.commands.module.ModuleCommandInitializer;
import org.infinispan.container.DataContainer;
import org.infinispan.distribution.DistributionManager;
import org.infinispan.factories.annotations.Inject;
import org.infinispan.samplemodule.commands.nonvisitable.PerformGCCommand;
import org.infinispan.samplemodule.commands.visitable.BulkDeleteCommand;
import org.infinispan.samplemodule.commands.visitable.PrintContentsCommand;

/**
 * Initializes remote commands - and also used as a builder for commands locally.
 *
 * @author Manik Surtani
 * @since 5.0
 */
public class CommandInitializer implements ModuleCommandInitializer {

   private DistributionManager distributionManager;
   private DataContainer dataContainer;
   private String cacheName;

   @Inject
   public void inject(DistributionManager distributionManager, DataContainer dataContainer, Cache cache) {
      this.distributionManager = distributionManager;
      this.dataContainer = dataContainer;
      this.cacheName = cache.getName();
   }

   public BulkDeleteCommand buildBulkDeleteCommand(String pattern) {
      return new BulkDeleteCommand(dataContainer, pattern, cacheName);
   }

   public PrintContentsCommand buildPrintContentsCommand() {
      return new PrintContentsCommand(dataContainer, cacheName);
   }

   public PerformGCCommand buildPerformGCCommand() {
      return new PerformGCCommand(distributionManager);
   }

   public void initializeReplicableCommand(ReplicableCommand c, boolean isRemote) {
      if (c instanceof BulkDeleteCommand)
         ((BulkDeleteCommand) c).injectComponents(dataContainer);
      else if (c instanceof PrintContentsCommand)
         ((PrintContentsCommand) c).injectComponents(dataContainer);
      else if (c instanceof PerformGCCommand)
         ((PerformGCCommand) c).inject(distributionManager);
   }
}
