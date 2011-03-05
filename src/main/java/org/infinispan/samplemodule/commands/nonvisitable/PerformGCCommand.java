package org.infinispan.samplemodule.commands.nonvisitable;

import org.infinispan.commands.CommandsFactory;
import org.infinispan.commands.ReplicableCommand;
import org.infinispan.context.InvocationContext;
import org.infinispan.distribution.DistributionManager;

/**
 * An example of a replicable, non-visitable command that tries to prod the JVM to do a garbage collection.
 *
 * @author Manik Surtani
 * @since 5.0
 */
public class PerformGCCommand implements ReplicableCommand {
   public static final byte COMMAND_ID = 100;

   // Unused component in this example, but injecting it anyway as a demonstration.
   private DistributionManager distributionManager;

   /**
    * Need an empty ctor to facilitate deserialisation
    */
   public PerformGCCommand() {
   }

   /**
    * Ctor with any components needed, so the {@link CommandsFactory} can easily inject dependencies.
    * @param distributionManager Distribution manager
    */
   public PerformGCCommand(DistributionManager distributionManager) {
      this.distributionManager = distributionManager;
   }

   /**
    * Provide additional mechanism for injecting components after deserialising.
    * @param distributionManager Distribution manager
    */
   public void inject(DistributionManager distributionManager) {
      this.distributionManager = distributionManager;
   }


   @Override
   public Object perform(InvocationContext ctx) throws Throwable {
      System.gc();
      return null;
   }

   @Override
   public byte getCommandId() {
      return COMMAND_ID;
   }

   @Override
   public Object[] getParameters() {
      return new Object[0];
   }

   @Override
   public void setParameters(int commandId, Object[] parameters) {
   }
}
