/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.twitter.api;

/**
 * Created by Bogomolov Vladimir on 20.10.17.
 */
public class SizeObject {
    private Integer height;
    private Integer width;
    private ResizeType resize;

    public SizeObject() {
    }

    public SizeObject(ImageSize type, Integer height, Integer width, ResizeType resize) {
        this.height = height;
        this.width = width;
        this.resize = resize;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public ResizeType getResize() {
        return resize;
    }

    public void setResize(ResizeType resize) {
        this.resize = resize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SizeObject that = (SizeObject) o;

        if (height != null ? !height.equals(that.height) : that.height != null) return false;
        if (width != null ? !width.equals(that.width) : that.width != null) return false;
        return resize == that.resize;
    }

    @Override
    public int hashCode() {
        int result = height != null ? height.hashCode() : 0;
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (resize != null ? resize.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SizeObject{" +
                ", height=" + height +
                ", width=" + width +
                ", resize=" + resize +
                "}\n";
    }
}
