package org.infinispan.samplemodule.commands.visitable;

import org.infinispan.commands.CommandsFactory;
import org.infinispan.commands.VisitableCommand;
import org.infinispan.commands.Visitor;
import org.infinispan.container.DataContainer;
import org.infinispan.context.InvocationContext;
import org.infinispan.samplemodule.visitors.SampleModuleVisitor;

/**
 * This is a command that prints the contents of the cache onto stdout.
 *
 * @author Manik Surtani
 * @since 5.0
 */
public class PrintContentsCommand implements VisitableCommand {

   public static final byte COMMAND_ID = 101;

   private DataContainer dataContainer;

   /**
    * Need an empty ctor to facilitate deserialisation
    */
   public PrintContentsCommand() {
   }

   /**
    * Ctor with any components needed, so the {@link CommandsFactory} can easily inject dependencies.
    * @param dataContainer data container
    */
   public PrintContentsCommand(DataContainer dataContainer) {
      injectComponents(dataContainer);
   }

   /**
    * Provide additional mechanism for injecting components after deserialising.
    * @param dataContainer data container
    */
   public void injectComponents(DataContainer dataContainer) {
      this.dataContainer = dataContainer;
   }

   @Override
   public Object acceptVisitor(InvocationContext ctx, Visitor visitor) throws Throwable {
      if (visitor instanceof SampleModuleVisitor) {
         return ((SampleModuleVisitor) visitor).visitPrintContentsCommand(ctx, this);
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
      return String.format("Contents of the cache (%s keys):%n%s", dataContainer.size(), dataContainer.keySet());
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
