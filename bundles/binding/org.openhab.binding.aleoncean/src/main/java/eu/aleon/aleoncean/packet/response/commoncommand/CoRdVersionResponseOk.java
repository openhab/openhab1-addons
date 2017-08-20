/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.packet.response.commoncommand;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import eu.aleon.aleoncean.packet.EnOceanId;
import eu.aleon.aleoncean.packet.response.Response;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class CoRdVersionResponseOk extends Response {

    private static final int CHIP_VERSION_LENGTH = 4;
    private static final int APP_DESCRIPTION_LENGTH = 16;

    private byte appVersionMain;
    private byte appVersionBeta;
    private byte appVersionAlpha;
    private byte appVersionBuild;

    private byte apiVersionMain;
    private byte apiVersionBeta;
    private byte apiVersionAlpha;
    private byte apiVersionBuild;

    private final EnOceanId chipId = new EnOceanId();

    private final byte[] chipVersion = new byte[CHIP_VERSION_LENGTH];

    private final byte[] appDescription = new byte[APP_DESCRIPTION_LENGTH];

    public CoRdVersionResponseOk() {
    }

    @Override
    public byte[] getResponseData() {
        final int rawResponseDataLength = 4 /* app version (main, beta, alpha, build) */
                                          + 4 /* api version (main, beta, alpha, build) */
                                          + EnOceanId.LENGTH
                                          + chipVersion.length
                                          + appDescription.length;

        final byte[] rawResponseData = new byte[rawResponseDataLength];
        final ByteBuffer bb = ByteBuffer.wrap(rawResponseData);
        bb.order(ByteOrder.BIG_ENDIAN);

        bb.put(getAppVersionMain());
        bb.put(getAppVersionBeta());
        bb.put(getAppVersionAlpha());
        bb.put(getAppVersionBuild());

        bb.put(getApiVersionMain());
        bb.put(getApiVersionBeta());
        bb.put(getApiVersionAlpha());
        bb.put(getApiVersionBuild());

        bb.put(getChipId().getBytes());

        bb.put(getChipVersion());

        bb.put(getAppDescription());

        return rawResponseData;
    }

    @Override
    public void setResponseData(final byte[] responseData) {
        byte[] tmp;

        final ByteBuffer bb = ByteBuffer.wrap(responseData);
        bb.order(ByteOrder.BIG_ENDIAN);

        setAppVersionMain(bb.get());
        setAppVersionBeta(bb.get());
        setAppVersionAlpha(bb.get());
        setAppVersionBuild(bb.get());

        setApiVersionMain(bb.get());
        setApiVersionBeta(bb.get());
        setApiVersionAlpha(bb.get());
        setApiVersionBuild(bb.get());

        setChipId(new EnOceanId(bb));

        tmp = new byte[CHIP_VERSION_LENGTH];
        bb.get(tmp);
        setChipVersion(tmp);

        tmp = new byte[APP_DESCRIPTION_LENGTH];
        bb.get(tmp);
        setAppDescription(tmp);
    }

    @Override
    public byte[] getOptionalData() {
        return null;
    }

    @Override
    public void setOptionalData(final byte[] optionalData) {
    }

    public byte getAppVersionMain() {
        return appVersionMain;
    }

    public void setAppVersionMain(final byte appVersionMain) {
        this.appVersionMain = appVersionMain;
    }

    public byte getAppVersionBeta() {
        return appVersionBeta;
    }

    public void setAppVersionBeta(final byte appVersionBeta) {
        this.appVersionBeta = appVersionBeta;
    }

    public byte getAppVersionAlpha() {
        return appVersionAlpha;
    }

    public void setAppVersionAlpha(final byte appVersionAlpha) {
        this.appVersionAlpha = appVersionAlpha;
    }

    public byte getAppVersionBuild() {
        return appVersionBuild;
    }

    public void setAppVersionBuild(final byte appVersionBuild) {
        this.appVersionBuild = appVersionBuild;
    }

    public byte getApiVersionMain() {
        return apiVersionMain;
    }

    public void setApiVersionMain(final byte apiVersionMain) {
        this.apiVersionMain = apiVersionMain;
    }

    public byte getApiVersionBeta() {
        return apiVersionBeta;
    }

    public void setApiVersionBeta(final byte apiVersionBeta) {
        this.apiVersionBeta = apiVersionBeta;
    }

    public byte getApiVersionAlpha() {
        return apiVersionAlpha;
    }

    public void setApiVersionAlpha(final byte apiVersionAlpha) {
        this.apiVersionAlpha = apiVersionAlpha;
    }

    public byte getApiVersionBuild() {
        return apiVersionBuild;
    }

    public void setApiVersionBuild(final byte apiVersionBuild) {
        this.apiVersionBuild = apiVersionBuild;
    }

    public EnOceanId getChipId() {
        return chipId;
    }

    public void setChipId(final EnOceanId id) {
        chipId.fill(id);
    }

    public byte[] getChipVersion() {
        return chipVersion;
    }

    public void setChipVersion(final byte[] chipVersion) {
        final int toCopy = Math.min(chipVersion.length, this.chipVersion.length);
        final int toZero = this.chipVersion.length - toCopy;
        System.arraycopy(chipVersion, 0, this.chipVersion, 0, toCopy);
        Arrays.fill(this.chipVersion, toCopy, toCopy + toZero, (byte) 0);
    }

    public byte[] getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(final byte[] appDescription) {
        final int toCopy = Math.min(appDescription.length, this.appDescription.length);
        final int toZero = this.appDescription.length - toCopy;
        System.arraycopy(appDescription, 0, this.appDescription, 0, toCopy);
        Arrays.fill(this.appDescription, toCopy, toCopy + toZero, (byte) 0);
    }

}
