package org.infinispan.samplemodule;

import org.infinispan.commands.ReplicableCommand;
import org.infinispan.commands.module.ExtendedModuleCommandFactory;
import org.infinispan.commands.module.ModuleCommandFactory;
import org.infinispan.commands.remote.CacheRpcCommand;
import org.infinispan.samplemodule.commands.nonvisitable.PerformGCCommand;
import org.infinispan.samplemodule.commands.visitable.BulkDeleteCommand;
import org.infinispan.samplemodule.commands.visitable.PrintContentsCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Remote commands factory implementation
 *
 * @author Manik Surtani
 * @since 5.0
 */
public class CommandFactory implements ExtendedModuleCommandFactory {

   @Override
   public Map<Byte, Class<? extends ReplicableCommand>> getModuleCommands() {
      Map<Byte, Class<? extends ReplicableCommand>> map = new HashMap<Byte, Class<? extends ReplicableCommand>>(4);
      map.put(BulkDeleteCommand.COMMAND_ID, BulkDeleteCommand.class);
      map.put(PrintContentsCommand.COMMAND_ID, PrintContentsCommand.class);
      map.put(PerformGCCommand.COMMAND_ID, PerformGCCommand.class);
      return map;
   }

   @Override
   public ReplicableCommand fromStream(byte commandId, Object[] args) {
      ReplicableCommand c;
      switch (commandId) {
         case PerformGCCommand.COMMAND_ID:
            c = new PerformGCCommand();
            break;
         default:
            throw new IllegalArgumentException("Not registered to handle command id " + commandId);
      }
      c.setParameters(commandId, args);
      return c;
   }

   @Override
   public CacheRpcCommand fromStream(byte commandId, Object[] args, String cacheName) {
      CacheRpcCommand c;
      switch (commandId) {
         case BulkDeleteCommand.COMMAND_ID:
            c = new BulkDeleteCommand(cacheName);
            break;
         case PrintContentsCommand.COMMAND_ID:
            c = new PrintContentsCommand(cacheName);
            break;
         default:
            throw new IllegalArgumentException("Not registered to handle command id " + commandId);
      }
      c.setParameters(commandId, args);
      return c;
   }

}
