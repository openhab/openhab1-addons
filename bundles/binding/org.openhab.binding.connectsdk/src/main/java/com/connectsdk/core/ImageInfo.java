/*
 * ImageInfo
 * Connect SDK
 *
 * Copyright (c) 2014 LG Electronics.
 * Created by Simon Gladkoskok on 14 August 2014
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.connectsdk.core;

/**
 * Normalized reference object for information about an image file. This object can be used to represent a media file (ex. icon, poster)
 * 
 */

public class ImageInfo {

    /**
     * Default constructor method.
     * @param url
     */

    public ImageInfo(String url) {
        super();
        this.url = url;
    }

    /**
     * Default constructor method.
     * @param url, type, width, height
     *              add type of file, width and height of image.
     */

    public ImageInfo(String url, ImageType type, int width, int height) {
        this(url);
        this.type = type;
        this.width = width;
        this.height = height;
    }


    public enum ImageType {
        Thumb, Video_Poster, Album_Art, Unknown
    }

    private String url;
    private ImageType type;
    private int width;
    private int height;

    /**
     * Gets URL address of an image file.
     * 
     */

    public String getUrl() {
        return url;
    }


    /**
     * Sets URL address of an image file.
     * 
     */

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets a type of an image file.
     * 
     */

    public ImageType getType() {
        return type;
    }

    /**
     * Sets a type of an image file.
     * 
     */

    public void setType(ImageType type) {
        this.type = type;
    }

    /**
     * Gets a width of an image.
     * 
     */

    public int getWidth() {
        return width;
    }

    /**
     * Sets a width of an image.
     * 
     */

    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets a height of an image.
     * 
     */

    public int getHeight() {
        return height;
    }

    /**
     * Sets a height of an image.
     * 
     */

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ImageInfo imageInfo = (ImageInfo) o;

        return (getUrl() != null ? getUrl().equals(imageInfo.getUrl()) : imageInfo.getUrl() == null);

    }

    @Override
    public int hashCode() {
        return getUrl() != null ? getUrl().hashCode() : 0;
    }
}
