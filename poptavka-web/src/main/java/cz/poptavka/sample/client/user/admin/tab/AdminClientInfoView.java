/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.domain.user.Verification;
import cz.poptavka.sample.shared.domain.ClientDetail;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminClientInfoView extends Composite implements
        AdminClientInfoPresenter.AdminClientInfoInterface {

    private static AdminClientInfoViewUiBinder uiBinder = GWT.create(AdminClientInfoViewUiBinder.class);

    interface AdminClientInfoViewUiBinder extends
            UiBinder<Widget, AdminClientInfoView> {
    }
    // Supplier detail input fields
    @UiField
    TextArea descriptionBox;
    @UiField
    TextBox companyName, firstName, lastName, email, phone, overalRating, identifNumber, id;
    @UiField
    ListBox verification;
    // Supplier detail button fields
    @UiField
    Button createButton, updateButton;
    private ClientDetail clientInfo;

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public Button getUpdateBtn() {
        return updateButton;
    }

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        initClientInfoForm();
    }

    public ClientDetail getUpdatedClientDetail() {
        if (clientInfo == null) {
            return null;
        }
        // Update the client object.
        clientInfo.getUserDetail().setCompanyName(companyName.getText());
        clientInfo.getUserDetail().setDescription(descriptionBox.getText());
        clientInfo.getUserDetail().setFirstName(firstName.getText());
        clientInfo.getUserDetail().setLastName(lastName.getText());
        clientInfo.setVerification(verification.getItemText(verification.getSelectedIndex()));
        clientInfo.getUserDetail().setEmail(email.getText());
        clientInfo.getUserDetail().setPhone(phone.getText());
        clientInfo.setOveralRating(Integer.valueOf(overalRating.getText()));
        clientInfo.getUserDetail().setIdentificationNumber(identifNumber.getText());

        return clientInfo;
    }

    private void initClientInfoForm() {
        // initWidget(uiBinder.createAndBindUi(this));

        // Initialize the contact to null.
        setClientDetail(null);
    }

    public void setClientDetail(ClientDetail detail) {
        this.clientInfo = detail;
        updateButton.setEnabled(detail != null);
        if (detail != null) {
            //Company
            companyName.setText(detail.getUserDetail().getCompanyName());
            overalRating.setText(Integer.toString(detail.getOveralRating()));
            descriptionBox.setText(detail.getUserDetail().getDescription());
            // Verification.
            int i = 0;
            int j = 0;
            verification.clear();
            for (Verification type : Verification.values()) {
                verification.addItem(type.name());
                if (detail.getVerification() != null
                        && detail.getVerification().equalsIgnoreCase(type.name())) {
                    j = i;
                }
                i++;
            }
            verification.setSelectedIndex(j);
            //Contact
            firstName.setText(detail.getUserDetail().getFirstName());
            lastName.setText(detail.getUserDetail().getLastName());
            email.setText(detail.getUserDetail().getEmail());
            phone.setText(detail.getUserDetail().getPhone());

            //Busines data
            identifNumber.setText(detail.getUserDetail().getIdentificationNumber());
            id.setText(Long.toString(detail.getId()));
        }
    }
}
