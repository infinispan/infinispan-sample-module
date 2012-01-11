package org.infinispan.samplemodule;

import org.infinispan.commands.module.ExtendedModuleCommandFactory;
import org.infinispan.commands.module.ModuleCommandExtensions;
import org.infinispan.commands.module.ModuleCommandInitializer;

/**
 * Command extensions
 *
 * @author Galder Zamarreño
 * @since 5.1
 */
public class CommandExtensions implements ModuleCommandExtensions {

   @Override
   public ExtendedModuleCommandFactory getModuleCommandFactory() {
      return new CommandFactory();
   }

   @Override
   public ModuleCommandInitializer getModuleCommandInitializer() {
      return new CommandInitializer();
   }

}
