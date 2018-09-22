/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.pushbullet.internal.model;

import com.google.gson.annotations.SerializedName;

/**
 * This class represents the answer to pushes provided by the API.
 *
 * @author Hakan Tandogan
 * @since 1.11.0
 */
public class PushResponse {

    @SerializedName("active")
    private String active;

    @SerializedName("iden")
    private String iden;

    @SerializedName("type")
    private String type;

    @SerializedName("dismissed")
    private Boolean dismissed;

    @SerializedName("direction")
    private String direction;

    @SerializedName("sender_iden")
    private String senderIdentifier;

    @SerializedName("sender_email")
    private String senderEmail;

    @SerializedName("sender_email_normalized")
    private String senderEmailNormalized;

    @SerializedName("sender_name")
    private String senderName;

    @SerializedName("receiver_iden")
    private String receiverIdentifier;

    @SerializedName("receiver_email")
    private String receiverEmail;

    @SerializedName("receiver_email_normalized")
    private String receiverEmailNormalized;

    @SerializedName("title")
    private String title;

    @SerializedName("body")
    private String body;

    @SerializedName("error_code")
    private String errorCode;

    @SerializedName("error")
    private PushError pushError;

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getIden() {
        return iden;
    }

    public void setIden(String iden) {
        this.iden = iden;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getDismissed() {
        return dismissed;
    }

    public void setDismissed(Boolean dismissed) {
        this.dismissed = dismissed;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSenderIdentifier() {
        return senderIdentifier;
    }

    public void setSenderIdentifier(String senderIdentifier) {
        this.senderIdentifier = senderIdentifier;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderEmailNormalized() {
        return senderEmailNormalized;
    }

    public void setSenderEmailNormalized(String senderEmailNormalized) {
        this.senderEmailNormalized = senderEmailNormalized;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverIdentifier() {
        return receiverIdentifier;
    }

    public void setReceiverIdentifier(String receiverIdentifier) {
        this.receiverIdentifier = receiverIdentifier;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getReceiverEmailNormalized() {
        return receiverEmailNormalized;
    }

    public void setReceiverEmailNormalized(String receiverEmailNormalized) {
        this.receiverEmailNormalized = receiverEmailNormalized;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public PushError getPushError() {
        return pushError;
    }

    public void setPushError(PushError pushError) {
        this.pushError = pushError;
    }

    @Override
    public String toString() {
        return "PushResponse {" + "active='" + active + '\'' + ", iden='" + iden + '\'' + ", type='" + type + '\''
                + ", dismissed=" + dismissed + ", direction='" + direction + '\'' + ", senderIdentifier='"
                + senderIdentifier + '\'' + ", senderEmail='" + senderEmail + '\'' + ", senderEmailNormalized='"
                + senderEmailNormalized + '\'' + ", senderName='" + senderName + '\'' + ", receiverIdentifier='"
                + receiverIdentifier + '\'' + ", receiverEmail='" + receiverEmail + '\'' + ", receiverEmailNormalized='"
                + receiverEmailNormalized + '\'' + ", title='" + title + '\'' + ", body='" + body + '\''
                + ", errorCode='" + errorCode + '\'' + ", pushError=" + pushError + '}';
    }
}
