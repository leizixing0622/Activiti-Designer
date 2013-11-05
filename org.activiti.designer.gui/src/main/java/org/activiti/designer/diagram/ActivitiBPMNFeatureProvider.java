package org.activiti.designer.diagram;

import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.Artifact;
import org.activiti.bpmn.model.BoundaryEvent;
import org.activiti.bpmn.model.CallActivity;
import org.activiti.bpmn.model.Event;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Gateway;
import org.activiti.bpmn.model.Lane;
import org.activiti.bpmn.model.Pool;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.bpmn.model.Task;
import org.activiti.bpmn.model.TextAnnotation;
import org.activiti.designer.command.BoundaryEventModelUpdater;
import org.activiti.designer.command.BpmnProcessModelUpdater;
import org.activiti.designer.command.BusinessRuleTaskModelUpdater;
import org.activiti.designer.command.CallActivityModelUpdater;
import org.activiti.designer.command.EndEventModelUpdater;
import org.activiti.designer.command.GatewayModelUpdater;
import org.activiti.designer.command.IntermediateCatchEventModelUpdater;
import org.activiti.designer.command.LaneModelUpdater;
import org.activiti.designer.command.ManualTaskModelUpdater;
import org.activiti.designer.command.PoolModelUpdater;
import org.activiti.designer.command.ReceiveTaskModelUpdater;
import org.activiti.designer.command.ScriptTaskModelUpdater;
import org.activiti.designer.command.SendTaskModelUpdater;
import org.activiti.designer.command.ServiceTaskModelUpdater;
import org.activiti.designer.command.StartEventModelUpdater;
import org.activiti.designer.command.SubProcessModelUpdater;
import org.activiti.designer.command.ThrowEventModelUpdater;
import org.activiti.designer.command.UserTaskModelUpdater;
import org.activiti.designer.controller.BoundaryEventShapeController;
import org.activiti.designer.controller.BusinessObjectShapeController;
import org.activiti.designer.controller.CallActivityShapeController;
import org.activiti.designer.controller.CatchEventShapeController;
import org.activiti.designer.controller.EventBasedGatewayShapeController;
import org.activiti.designer.controller.EventShapeController;
import org.activiti.designer.controller.EventSubProcessShapeController;
import org.activiti.designer.controller.ExclusiveGatewayShapeController;
import org.activiti.designer.controller.InclusiveGatewayShapeController;
import org.activiti.designer.controller.LaneShapeController;
import org.activiti.designer.controller.ParallelGatewayShapeController;
import org.activiti.designer.controller.PoolShapeController;
import org.activiti.designer.controller.SequenceFlowShapeController;
import org.activiti.designer.controller.SubProcessShapeController;
import org.activiti.designer.controller.TaskShapeController;
import org.activiti.designer.controller.ThrowEventShapeController;
import org.activiti.designer.features.AddBaseElementFeature;
import org.activiti.designer.features.ChangeElementTypeFeature;
import org.activiti.designer.features.ContainerResizeFeature;
import org.activiti.designer.features.CopyFlowElementFeature;
import org.activiti.designer.features.CreateAssociationFeature;
import org.activiti.designer.features.CreateBoundaryErrorFeature;
import org.activiti.designer.features.CreateBoundaryMessageFeature;
import org.activiti.designer.features.CreateBoundarySignalFeature;
import org.activiti.designer.features.CreateBoundaryTimerFeature;
import org.activiti.designer.features.CreateBusinessRuleTaskFeature;
import org.activiti.designer.features.CreateCallActivityFeature;
import org.activiti.designer.features.CreateEmbeddedSubProcessFeature;
import org.activiti.designer.features.CreateEndEventFeature;
import org.activiti.designer.features.CreateErrorEndEventFeature;
import org.activiti.designer.features.CreateErrorStartEventFeature;
import org.activiti.designer.features.CreateEventGatewayFeature;
import org.activiti.designer.features.CreateEventSubProcessFeature;
import org.activiti.designer.features.CreateExclusiveGatewayFeature;
import org.activiti.designer.features.CreateInclusiveGatewayFeature;
import org.activiti.designer.features.CreateLaneFeature;
import org.activiti.designer.features.CreateMailTaskFeature;
import org.activiti.designer.features.CreateManualTaskFeature;
import org.activiti.designer.features.CreateMessageCatchingEventFeature;
import org.activiti.designer.features.CreateMessageStartEventFeature;
import org.activiti.designer.features.CreateNoneThrowingEventFeature;
import org.activiti.designer.features.CreateParallelGatewayFeature;
import org.activiti.designer.features.CreatePoolFeature;
import org.activiti.designer.features.CreateReceiveTaskFeature;
import org.activiti.designer.features.CreateScriptTaskFeature;
import org.activiti.designer.features.CreateSequenceFlowFeature;
import org.activiti.designer.features.CreateServiceTaskFeature;
import org.activiti.designer.features.CreateSignalCatchingEventFeature;
import org.activiti.designer.features.CreateSignalThrowingEventFeature;
import org.activiti.designer.features.CreateStartEventFeature;
import org.activiti.designer.features.CreateTerminateEndEventFeature;
import org.activiti.designer.features.CreateTextAnnotationFeature;
import org.activiti.designer.features.CreateTimerCatchingEventFeature;
import org.activiti.designer.features.CreateTimerStartEventFeature;
import org.activiti.designer.features.CreateUserTaskFeature;
import org.activiti.designer.features.DeleteArtifactFeature;
import org.activiti.designer.features.DeleteFlowElementFeature;
import org.activiti.designer.features.DeleteLaneFeature;
import org.activiti.designer.features.DeletePoolFeature;
import org.activiti.designer.features.DirectEditFlowElementFeature;
import org.activiti.designer.features.DirectEditTextAnnotationFeature;
import org.activiti.designer.features.LayoutTextAnnotationFeature;
import org.activiti.designer.features.MoveActivityFeature;
import org.activiti.designer.features.MoveBoundaryEventFeature;
import org.activiti.designer.features.MoveEventFeature;
import org.activiti.designer.features.MoveGatewayFeature;
import org.activiti.designer.features.MoveLaneFeature;
import org.activiti.designer.features.PasteFlowElementFeature;
import org.activiti.designer.features.ReconnectSequenceFlowFeature;
import org.activiti.designer.features.TaskResizeFeature;
import org.activiti.designer.features.UpdateFlowElementFeature;
import org.activiti.designer.features.UpdatePoolAndLaneFeature;
import org.activiti.designer.features.UpdateTextAnnotationFeature;
import org.activiti.designer.util.editor.BpmnIndependenceSolver;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICopyFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IPasteFeature;
import org.eclipse.graphiti.features.IReconnectionFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICopyContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IPasteContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.IReconnectionContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;

import com.alfresco.designer.gui.controller.AlfrescoStartEventShapeController;
import com.alfresco.designer.gui.controller.AlfrescoTaskShapeController;
import com.alfresco.designer.gui.features.CreateAlfrescoMailTaskFeature;
import com.alfresco.designer.gui.features.CreateAlfrescoScriptTaskFeature;
import com.alfresco.designer.gui.features.CreateAlfrescoStartEventFeature;
import com.alfresco.designer.gui.features.CreateAlfrescoUserTaskFeature;

public class ActivitiBPMNFeatureProvider extends DefaultFeatureProvider {

  protected List<BusinessObjectShapeController> shapeControllers;
  protected List<BpmnProcessModelUpdater> modelUpdaters;

  public ActivitiBPMNFeatureProvider(IDiagramTypeProvider dtp) {
    super(dtp);
    setIndependenceSolver(new BpmnIndependenceSolver(dtp));
    
    this.shapeControllers = new ArrayList<BusinessObjectShapeController>();
    shapeControllers.add(new EventShapeController(this));
    shapeControllers.add(new TaskShapeController(this));
    shapeControllers.add(new ExclusiveGatewayShapeController(this));
    shapeControllers.add(new EventBasedGatewayShapeController(this));
    shapeControllers.add(new InclusiveGatewayShapeController(this));
    shapeControllers.add(new ParallelGatewayShapeController(this));
    shapeControllers.add(new CatchEventShapeController(this));
    shapeControllers.add(new ThrowEventShapeController(this));
    shapeControllers.add(new SubProcessShapeController(this));
    shapeControllers.add(new CallActivityShapeController(this));
    shapeControllers.add(new EventSubProcessShapeController(this));
    shapeControllers.add(new BoundaryEventShapeController(this));
    shapeControllers.add(new PoolShapeController(this));
    shapeControllers.add(new LaneShapeController(this));
    shapeControllers.add(new SequenceFlowShapeController(this));
    shapeControllers.add(new AlfrescoStartEventShapeController(this));
    shapeControllers.add(new AlfrescoTaskShapeController(this));
    
    this.modelUpdaters = new ArrayList<BpmnProcessModelUpdater>();
    modelUpdaters.add(new StartEventModelUpdater(this));
    modelUpdaters.add(new EndEventModelUpdater(this));
    modelUpdaters.add(new UserTaskModelUpdater(this));
    modelUpdaters.add(new ServiceTaskModelUpdater(this));
    modelUpdaters.add(new ScriptTaskModelUpdater(this));
    modelUpdaters.add(new ReceiveTaskModelUpdater(this));
    modelUpdaters.add(new BusinessRuleTaskModelUpdater(this));
    modelUpdaters.add(new SendTaskModelUpdater(this));
    modelUpdaters.add(new ManualTaskModelUpdater(this));
    modelUpdaters.add(new GatewayModelUpdater(this));
    modelUpdaters.add(new IntermediateCatchEventModelUpdater(this));
    modelUpdaters.add(new ThrowEventModelUpdater(this));
    modelUpdaters.add(new CallActivityModelUpdater(this));
    modelUpdaters.add(new SubProcessModelUpdater(this));
    modelUpdaters.add(new BoundaryEventModelUpdater(this));
    modelUpdaters.add(new PoolModelUpdater(this));
    modelUpdaters.add(new LaneModelUpdater(this));
  }
  
  /**
   * @param businessObject object to get a {@link BusinessObjectShapeController} for
   * @return a {@link BusinessObjectShapeControllr} capable of creating/updating shapes
   * of for the given businessObject.
   * @throws IllegalArgumentException When no controller can be found for the given object.
   */
  public BusinessObjectShapeController getShapeController(Object businessObject) {
    for (BusinessObjectShapeController controller : shapeControllers) {
      if (controller.canControlShapeFor(businessObject)) {
        return controller;
      }
    }
    throw new IllegalArgumentException("No controller can be found for object: " + businessObject);
  }
  
  /**
   * @return true, if a {@link BusinessObjectShapeController} is available for the given business object.
   */
  public boolean hasShapeController(Object businessObject) {
    for (BusinessObjectShapeController controller : shapeControllers) {
        if (controller.canControlShapeFor(businessObject)) {
          return true;
        }
      }
    return false;
  }
  
  /**
   * @param businessObject the business-object to update
   * @param pictogramElement optional pictogram-element to refresh after update is performed. When null
   * is provided, no additional update besides the actual model update is done.
   * @return the updater capable of updating the given object. Null, if the object cannot be updated.
   */
  public BpmnProcessModelUpdater getModelUpdaterFor(Object businessObject, PictogramElement pictogramElement) {
    for (BpmnProcessModelUpdater updater : modelUpdaters) {
      if (updater.canControlShapeFor(businessObject)) {
        // creates a new BpmnProcessModelUpdater instances for undo/redo stack
        BpmnProcessModelUpdater updaterObject = updater.init(businessObject, pictogramElement);
        return updaterObject;
      }
    }
    throw new IllegalArgumentException("No updater can be found for object: " + businessObject);
  }

  @Override
  public IAddFeature getAddFeature(IAddContext context) {
    /*if (context.getNewObject() instanceof StartEvent) {
      if (context.getNewObject() instanceof AlfrescoStartEvent) {
        return new AddAlfrescoStartEventFeature(this);
      } else {
        StartEvent startEvent = (StartEvent) context.getNewObject();
        if (startEvent.getEventDefinitions().size() > 0) {
          if (startEvent.getEventDefinitions().get(0) instanceof TimerEventDefinition) {
            return new AddTimerStartEventFeature(this);
          }
          if (startEvent.getEventDefinitions().get(0) instanceof MessageEventDefinition) {
            return new AddMessageStartEventFeature(this);
          } else {
            return new AddErrorStartEventFeature(this);
          }
        } else {
          return new AddStartEventFeature(this);
        }
      }
    } else if (context.getNewObject() instanceof EndEvent) {
      EndEvent endEvent = (EndEvent) context.getNewObject();
      for (EventDefinition eventDefinition : endEvent.getEventDefinitions()) {
        if (eventDefinition instanceof ErrorEventDefinition) {
          return new AddErrorEndEventFeature(this);
        } else if (eventDefinition instanceof TerminateEventDefinition) {
          return new AddTerminateEndEventFeature(this);
        }
      }
      return new AddEndEventFeature(this);

    } else if (context.getNewObject() instanceof SequenceFlow) {
      return new AddSequenceFlowFeature(this);

    } else if (context.getNewObject() instanceof Association) {
      return new AddAssociationFeature(this);
    } else if (context.getNewObject() instanceof UserTask) {
      if (context.getNewObject() instanceof AlfrescoUserTask) {
        return new AddAlfrescoUserTaskFeature(this);
      } else {
        return new AddUserTaskFeature(this);
      }
    } else if (context.getNewObject() instanceof ManualTask) {
      return new AddManualTaskFeature(this);
    } else if (context.getNewObject() instanceof ReceiveTask) {
      return new AddReceiveTaskFeature(this);
    } else if (context.getNewObject() instanceof BusinessRuleTask) {
      return new AddBusinessRuleTaskFeature(this);
    } else if (context.getNewObject() instanceof ServiceTask) {
      ServiceTask serviceTask = (ServiceTask) context.getNewObject();
      if (ServiceTask.MAIL_TASK.equalsIgnoreCase(serviceTask.getType())) {
        return new AddMailTaskFeature(this);
      } else if (AlfrescoScriptTask.ALFRESCO_SCRIPT_DELEGATE.equalsIgnoreCase(serviceTask.getImplementation())) {
        boolean isMailTask = false;
        for (FieldExtension fieldExtension : serviceTask.getFieldExtensions()) {
          if ("script".equalsIgnoreCase(fieldExtension.getFieldName())) {
            if(fieldExtension.getStringValue() != null) {
              if (fieldExtension.getStringValue().contains("mail.execute(bpm_package);")) {
                isMailTask = true;
              }
            } else if(fieldExtension.getExpression() != null) {
              if (fieldExtension.getExpression().contains("mail.execute(bpm_package);")) {
                isMailTask = true;
              }
            }
          }
        }
        if (isMailTask) {
          return new AddAlfrescoMailTaskFeature(this);
        } else {
          return new AddAlfrescoScriptTaskFeature(this);
        }
      } else {
        return new AddServiceTaskFeature(this);
      }
    } else if (context.getNewObject() instanceof ScriptTask) {
      return new AddScriptTaskFeature(this);
    } else if (context.getNewObject() instanceof ExclusiveGateway) {
      return new AddExclusiveGatewayFeature(this);
    } else if (context.getNewObject() instanceof InclusiveGateway) {
      return new AddInclusiveGatewayFeature(this);
    } else if (context.getNewObject() instanceof ParallelGateway) {
      return new AddParallelGatewayFeature(this);
    } else if (context.getNewObject() instanceof EventGateway) {
      return new AddEventGatewayFeature(this);
    } else if (context.getNewObject() instanceof BoundaryEvent) {
      if (((BoundaryEvent) context.getNewObject()).getEventDefinitions().size() > 0) {
        EventDefinition definition = ((BoundaryEvent) context.getNewObject()).getEventDefinitions().get(0);
        if (definition instanceof ErrorEventDefinition) {
          return new AddBoundaryErrorFeature(this);
        } else if (definition instanceof SignalEventDefinition) {
          return new AddBoundarySignalFeature(this);
        } else if (definition instanceof MessageEventDefinition) {
          return new AddBoundaryMessageFeature(this);
        } else {
          return new AddBoundaryTimerFeature(this);
        }
      }
    } else if (context.getNewObject() instanceof IntermediateCatchEvent) {
      if (((IntermediateCatchEvent) context.getNewObject()).getEventDefinitions().size() > 0) {
        EventDefinition definition = ((IntermediateCatchEvent) context.getNewObject()).getEventDefinitions().get(0);
        if (definition instanceof SignalEventDefinition) {
          return new AddSignalCatchingEventFeature(this);
        } else if (definition instanceof MessageEventDefinition) {
          return new AddMessageCatchingEventFeature(this);
        } else {
          return new AddTimerCatchingEventFeature(this);
        }
      }
    } else if (context.getNewObject() instanceof ThrowEvent) {
      if (((ThrowEvent) context.getNewObject()).getEventDefinitions().size() > 0) {
        return new AddSignalThrowingEventFeature(this);
      } else {
        return new AddNoneThrowingEventFeature(this);
      }
    } else if (context.getNewObject() instanceof EventSubProcess) {
      return new AddEventSubProcessFeature(this);
    } else if (context.getNewObject() instanceof SubProcess) {
      return new AddEmbeddedSubProcessFeature(this);
    } else if (context.getNewObject() instanceof Pool) {
      return new AddPoolFeature(this);
    } else if (context.getNewObject() instanceof Lane) {
      return new AddLaneFeature(this);
    } else if (context.getNewObject() instanceof CallActivity) {
      return new AddCallActivityFeature(this);
    } else if (context.getNewObject() instanceof TextAnnotation) {
      return new AddTextAnnotationFeature(this);
    }*/
    return new AddBaseElementFeature(this);
  }

  @Override
  public ICreateFeature[] getCreateFeatures() {
    return new ICreateFeature[] { new CreateAlfrescoStartEventFeature(this), new CreateStartEventFeature(this), new CreateTimerStartEventFeature(this),
        new CreateMessageStartEventFeature(this), new CreateErrorStartEventFeature(this), new CreateEndEventFeature(this),
        new CreateErrorEndEventFeature(this), new CreateTerminateEndEventFeature(this), new CreateUserTaskFeature(this),
        new CreateAlfrescoUserTaskFeature(this), new CreateScriptTaskFeature(this), new CreateServiceTaskFeature(this), new CreateMailTaskFeature(this),
        new CreateManualTaskFeature(this), new CreateReceiveTaskFeature(this), new CreateBusinessRuleTaskFeature(this), new CreateParallelGatewayFeature(this),
        new CreateExclusiveGatewayFeature(this), new CreateInclusiveGatewayFeature(this), new CreateEventGatewayFeature(this),
        new CreateBoundaryTimerFeature(this), new CreateBoundaryErrorFeature(this), new CreateBoundaryMessageFeature(this),
        new CreateBoundarySignalFeature(this), new CreateTimerCatchingEventFeature(this), new CreateSignalCatchingEventFeature(this),
        new CreateMessageCatchingEventFeature(this), new CreateSignalThrowingEventFeature(this), new CreateNoneThrowingEventFeature(this),
        new CreateEventSubProcessFeature(this), new CreateEmbeddedSubProcessFeature(this), new CreatePoolFeature(this), new CreateLaneFeature(this),
        new CreateCallActivityFeature(this), new CreateAlfrescoScriptTaskFeature(this), new CreateAlfrescoMailTaskFeature(this),
        new CreateTextAnnotationFeature(this) };
  }

  @Override
  public IDeleteFeature getDeleteFeature(IDeleteContext context) {
    PictogramElement pictogramElement = context.getPictogramElement();
    Object bo = getBusinessObjectForPictogramElement(pictogramElement);

    if (bo instanceof FlowElement) {
      return new DeleteFlowElementFeature(this);
    } else if (bo instanceof Lane || bo instanceof Pool) {
      return new DeleteLaneFeature(this);
    } else if (bo instanceof Artifact) {
      return new DeleteArtifactFeature(this);
    }
    return super.getDeleteFeature(context);
  }

  @Override
  public ICopyFeature getCopyFeature(ICopyContext context) {
    return new CopyFlowElementFeature(this);
  }

  @Override
  public IPasteFeature getPasteFeature(IPasteContext context) {
    return new PasteFlowElementFeature(this);
  }

  @Override
  public ICreateConnectionFeature[] getCreateConnectionFeatures() {

    return new ICreateConnectionFeature[] { new CreateSequenceFlowFeature(this), new CreateAssociationFeature(this) };
  }

  @Override
  public IReconnectionFeature getReconnectionFeature(IReconnectionContext context) {
    return new ReconnectSequenceFlowFeature(this);
  }

  @Override
  public IUpdateFeature getUpdateFeature(IUpdateContext context) {
    PictogramElement pictogramElement = context.getPictogramElement();
    Object bo = getBusinessObjectForPictogramElement(pictogramElement);

    if (pictogramElement instanceof ContainerShape) {
      if (bo instanceof FlowElement) {
        return new UpdateFlowElementFeature(this);
      } else if (bo instanceof Pool || bo instanceof Lane) {
        return new UpdatePoolAndLaneFeature(this);
      } else if (bo instanceof TextAnnotation) {
        return new UpdateTextAnnotationFeature(this);
      }
    }
    return super.getUpdateFeature(context);
  }

  @Override
  public IFeature[] getDragAndDropFeatures(IPictogramElementContext context) {
    // simply return all create connection features
    return getCreateConnectionFeatures();
  }

  @Override
  public IDirectEditingFeature getDirectEditingFeature(IDirectEditingContext context) {
    PictogramElement pe = context.getPictogramElement();
    Object bo = getBusinessObjectForPictogramElement(pe);
    if (bo instanceof FlowElement) {
      return new DirectEditFlowElementFeature(this);
    } else if (bo instanceof TextAnnotation) {
      return new DirectEditTextAnnotationFeature(this);
    }
    return super.getDirectEditingFeature(context);
  }

  @Override
  public IResizeShapeFeature getResizeShapeFeature(IResizeShapeContext context) {
    Shape shape = context.getShape();
    Object bo = getBusinessObjectForPictogramElement(shape);
    if (bo instanceof SubProcess || bo instanceof Pool || bo instanceof Lane) {
      return new ContainerResizeFeature(this);
    } else if (bo instanceof Task || bo instanceof CallActivity) {
      return new TaskResizeFeature(this);
    }
    return super.getResizeShapeFeature(context);
  }

  @Override
  public IMoveShapeFeature getMoveShapeFeature(IMoveShapeContext context) {
    Shape shape = context.getShape();
    Object bo = getBusinessObjectForPictogramElement(shape);
    if (bo instanceof BoundaryEvent) {
      return new MoveBoundaryEventFeature(this);

    } else if (bo instanceof Activity) {
      // in case an activity is moved, make sure, attached boundary events will
      // move too
      return new MoveActivityFeature(this);

    } else if (bo instanceof Gateway) {
      return new MoveGatewayFeature(this);

    } else if (bo instanceof Event) {
      return new MoveEventFeature(this);

    } else if (bo instanceof Lane) {
      return new MoveLaneFeature(this);
    }
    return super.getMoveShapeFeature(context);
  }

  @Override
  public ILayoutFeature getLayoutFeature(ILayoutContext context) {
    final PictogramElement pe = context.getPictogramElement();
    final Object bo = getBusinessObjectForPictogramElement(pe);

    if (bo instanceof TextAnnotation) {
      return new LayoutTextAnnotationFeature(this);
    }

    return super.getLayoutFeature(context);
  }

  @Override
  public ICustomFeature[] getCustomFeatures(ICustomContext context) {
    return new ICustomFeature[] { new DeletePoolFeature(this), new ChangeElementTypeFeature(this) };
  }
}
