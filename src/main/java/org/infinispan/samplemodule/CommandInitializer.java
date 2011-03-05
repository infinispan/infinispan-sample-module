package org.infinispan.samplemodule;

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

   @Inject
   public void inject(DistributionManager distributionManager, DataContainer dataContainer) {
      this.distributionManager = distributionManager;
      this.dataContainer = dataContainer;
   }

   public BulkDeleteCommand buildBulkDeleteCommand(String pattern) {
      return new BulkDeleteCommand(dataContainer, pattern);
   }

   public PrintContentsCommand buildPrintContentsCommand() {
      return new PrintContentsCommand(dataContainer);
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
