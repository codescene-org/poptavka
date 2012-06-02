package com.eprovement.poptavka.client.home.creation;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import com.eprovement.poptavka.client.home.creation.FormUserRegistrationPresenter.FormRegistrationInterface;
import com.eprovement.poptavka.client.main.common.StatusIconLabel;
import com.eprovement.poptavka.client.main.common.StatusIconLabel.State;
import com.eprovement.poptavka.client.main.common.category.CategorySelectorPresenter.CategorySelectorInterface;
import com.eprovement.poptavka.client.main.common.creation.FormDemandAdvPresenter;
import com.eprovement.poptavka.client.main.common.creation.FormDemandAdvPresenter.FormDemandAdvViewInterface;
import com.eprovement.poptavka.client.main.common.creation.FormDemandBasicPresenter;
import com.eprovement.poptavka.client.main.common.creation.FormDemandBasicPresenter.FormDemandBasicInterface;
import com.eprovement.poptavka.client.main.common.creation.ProvidesValidate;
import com.eprovement.poptavka.client.main.common.locality.LocalitySelectorPresenter.LocalitySelectorInterface;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import java.util.HashMap;
import java.util.Map;

@Presenter(view = DemandCreationView.class)
public class DemandCreationPresenter
        extends LazyPresenter<DemandCreationPresenter.CreationViewInterface, DemandCreationEventBus> {

    private final static Logger LOGGER = Logger.getLogger("DemandCreationPresenter");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    // TODO praso - All this presenters should be moved into this particular
    // module otherwise they will fall in left-over fragment
    private FormDemandBasicPresenter demandBasicForm = null;
    private FormDemandAdvPresenter demandAdvForm = null;

    public interface CreationViewInterface extends LazyView {

        StackLayoutPanel getMainPanel();

        Widget getWidgetView();

        void toggleLoginRegistration();

        StatusIconLabel getStatusLabel(int order);

        SimplePanel getHolderPanel(int order);

        HasClickHandlers getCreateDemandButton();
    }

    @Override
    public void bindView() {
        view.getMainPanel().addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {

            @Override
            public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
                int eventItem = event.getItem();
                if (view.getMainPanel().getVisibleIndex() < eventItem) {
                    boolean result = canContinue(eventItem);
                    if (!result) {
                        // TODO change to global status changer eventBus call
                        event.cancel();
                    } else {
                        // TODO change to global status changer eventBus call
                        view.getStatusLabel(eventItem).setPassedSmall(result);
                    }
                }
            }
        });
        view.getCreateDemandButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (canContinue(LOGIN)) {
                    registerNewCient();
                }
            }
        });

    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    public void onForward() {
        eventBus.setUpSearchBar(null, false, false, false);
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    public void onGoToCreateDemandModule() {
        LOGGER.info("  INIT DemandCreation Widget");
        view.getMainPanel().showWidget(0);
        eventBus.initDemandBasicForm(view.getHolderPanel(BASIC));
        eventBus.initCategoryWidget(view.getHolderPanel(CATEGORY));
        // TODO Praso - what is this method supposed to do? Does it loads widgets in advance?
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            @Override
            public void execute() {
                //TODO praso - these methods are currently in presenter itself and there is
                // no need to call them via eventbus.
                eventBus.initLocalityWidget(view.getHolderPanel(LOCALITY));
                eventBus.initDemandAdvForm(view.getHolderPanel(ADVANCED));
                eventBus.initLoginForm(view.getHolderPanel(LOGIN));
            }
        });
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    public void onInitDemandBasicForm(SimplePanel holderWidget) {
        if (demandBasicForm != null) {
            eventBus.removeHandler(demandBasicForm);
        }
        demandBasicForm = eventBus.addHandler(FormDemandBasicPresenter.class);
        demandBasicForm.initDemandBasicForm(holderWidget);
    }

    public void onInitDemandAdvForm(SimplePanel holderWidget) {
        if (demandAdvForm != null) {
            eventBus.removeHandler(demandAdvForm);
        }
        demandAdvForm = eventBus.addHandler(FormDemandAdvPresenter.class);
        demandAdvForm.initDemandAdvForm(holderWidget);
    }

    /**
     * Called from DemandCreationHandler after successful login or registration
     * of new client. As first a new client is created through DemandCreationHandler
     * and after successful registration (or login of existing client) this method
     * is initiated by DemandCreationHandler to create new Demand for this client.
     *
     * @param client detail object that was either created (if new client) or
     * retried from database (if existing client)
     */
    public void onPrepareNewDemandForNewClient(UserDetail client) {
        eventBus.loadingShow(MSGS.progressGettingDemandData());

        FormDemandBasicInterface basicValues =
                (FormDemandBasicInterface) view.getHolderPanel(BASIC).getWidget();
        CategorySelectorInterface categoryValues =
                (CategorySelectorInterface) view.getHolderPanel(CATEGORY).getWidget();
        LocalitySelectorInterface localityValues =
                (LocalitySelectorInterface) view.getHolderPanel(LOCALITY).getWidget();
        FormDemandAdvViewInterface advValues =
                (FormDemandAdvViewInterface) view.getHolderPanel(ADVANCED).getWidget();

        // Fill in the FullDemandDetail obejct from former holder panels.
        FullDemandDetail demand = new FullDemandDetail();
        demand.setBasicInfo(basicValues.getValues());

        //localities
        Map<String, String> localities = new HashMap<String, String>();
        for (int i = 0; i < localityValues.getSelectedList().getItemCount(); i++) {
            localities.put(
                    localityValues.getSelectedList().getValue(i),
                    localityValues.getSelectedList().getItemText(i));
        }
        demand.setLocalities(localities);

        //categories
        Map<Long, String> categories = new HashMap<Long, String>();
        for (CategoryDetail catDetail: categoryValues.getDataProvider().getList()) {
            categories.put(catDetail.getId(), catDetail.getName());
        }
        demand.setCategories(categories);

        demand.setAdvInfo(advValues.getValues());
        eventBus.createDemand(demand, client.getClientId());
        // TODO Praso - improve loaging method. We can for example show some message to client while waiting
        eventBus.loadingShow(MSGS.progressCreatingDemand());

    }

    /** Done automatically in step five, when option: register new client is selected. **/
    //DO NOT EDIT
    public void onToggleLoginRegistration() {
        view.toggleLoginRegistration();
    }

    /** showing error after login failure. **/
    public void onLoginError() {
        // TODO change to global status changer eventBus call
        view.getStatusLabel(LOGIN).setStyleState(StyleResource.INSTANCE.common().errorMessage(), State.ERROR_16);
        view.getStatusLabel(LOGIN).setTexts(MSGS.wrongLoginMessage(), MSGS.wrongLoginDescription());
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    /**
     * In the process of creating a new demand by brand new client the method
     * registerNewClient() is called as first. When the registration of new client
     * is successful the demand will be created in the next step. See method
     * onPrepareNewDemandForNewClient().
     */
    private void registerNewCient() {
        //signal event
        eventBus.loadingShow(MSGS.progressRegisterClient());
        // ClientDetail instance
        FormRegistrationInterface registerWidget = (FormRegistrationInterface) view.getHolderPanel(LOGIN).getWidget();
        UserDetail newClient = registerWidget.getNewClient();
        eventBus.registerNewClient(newClient);

    }
    private static final int BASIC = 1;
    private static final int CATEGORY = 2;
    private static final int LOCALITY = 3;
    private static final int ADVANCED = 4;
    private static final int LOGIN = 5;

    private boolean canContinue(int step) {
        ProvidesValidate widget = (ProvidesValidate) view.getHolderPanel(step).getWidget();
        LOGGER.fine(widget.getClass().getName());
        return widget.isValid();
    }
}