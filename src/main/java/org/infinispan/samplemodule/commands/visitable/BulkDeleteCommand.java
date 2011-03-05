package org.infinispan.samplemodule.commands.visitable;

import org.infinispan.commands.CommandsFactory;
import org.infinispan.commands.VisitableCommand;
import org.infinispan.commands.Visitor;
import org.infinispan.container.DataContainer;
import org.infinispan.context.InvocationContext;
import org.infinispan.samplemodule.visitors.SampleModuleVisitor;

/**
 * This command removes entries based on a RegEx pattern passed in.
 *
 * @author Manik Surtani
 * @since 5.0
 */
public class BulkDeleteCommand implements VisitableCommand {

   public static final byte COMMAND_ID = 102;

   private DataContainer dataContainer;
   private String regexPattern;

   /**
    * Need an empty ctor to facilitate deserialisation
    */
   public BulkDeleteCommand() {
   }

   /**
    * Ctor with any components needed, so the {@link CommandsFactory} can easily inject dependencies.
    * @param dataContainer data container
    */
   public BulkDeleteCommand(DataContainer dataContainer, String regexPattern) {
      this.dataContainer = dataContainer;
      this.regexPattern = regexPattern;
   }

   /**
    * Provide additional mechanism for injecting components after deserialising.
    * @param dataContainer data container
    */
   public void injectComponents(DataContainer dataContainer) {
      this.dataContainer = dataContainer;
   }

   public String getPattern() {
      return regexPattern;
   }

   @Override
   public Object acceptVisitor(InvocationContext ctx, Visitor visitor) throws Throwable {
      if (visitor instanceof SampleModuleVisitor) {
         return ((SampleModuleVisitor) visitor).visitBulkDeleteCommand(ctx, this);
      } else {
         return visitor.visitUnknownCommand(ctx, this);
      }
   }

   @Override
   public boolean shouldInvoke(InvocationContext ctx) {
      return true;
   }

   @Override
   public Object perform(InvocationContext ctx) throws Throwable {
      for (Object key: dataContainer.keySet()) {
         if (key.toString().matches(regexPattern))
            dataContainer.remove(key);
      }

      return null;
   }

   @Override
   public byte getCommandId() {
      return COMMAND_ID;
   }

   @Override
   public Object[] getParameters() {
      return new Object[]{regexPattern};
   }

   @Override
   public void setParameters(int commandId, Object[] parameters) {
      regexPattern = parameters[0].toString();
   }
}
