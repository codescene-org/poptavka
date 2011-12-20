package cz.poptavka.sample.client.root.interfaces;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.IsWidget;

public interface IHeaderView extends IsWidget {
    public interface IHeaderPresenter {

    }

    Anchor getLoginLink();

    void toggleMainLayout(boolean switchToUserLayout);

}
