// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.github.fscheffer.arras.components;

import java.util.Map;

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;

import com.github.fscheffer.arras.IconEffect;

@SupportsInformalParameters
@Import(stylesheet = "font-awesome/css/font-awesome.css")
public class Icon {

    private static Map<String, String> fallback = CollectionFactory.newMap();

    {
        fallback.put("ban-circle", "ban");
        fallback.put("bar-chart", "bar-chart-o");
        fallback.put("beaker", "flask");
        fallback.put("bell", "bell-o");
        fallback.put("bell-alt", "bell");
        fallback.put("bitbucket-sign", "bitbucket-square");
        fallback.put("bookmark-empty", "bookmark-o");
        fallback.put("building", "building-o");
        fallback.put("calendar-empty", "calendar-o");
        fallback.put("check-empty", "square-o");
        fallback.put("check-minus", "minus-square-o");
        fallback.put("check-sign", "check-square");
        fallback.put("check", "check-square-o");
        fallback.put("chevron-sign-down", "chevron-down");
        fallback.put("chevron-sign-left", "chevron-left");
        fallback.put("chevron-sign-right", "chevron-right");
        fallback.put("chevron-sign-up", "chevron-up");
        fallback.put("circle-arrow-down", "arrow-circle-down");
        fallback.put("circle-arrow-left", "arrow-circle-left");
        fallback.put("circle-arrow-right", "arrow-circle-right");
        fallback.put("circle-arrow-up", "arrow-circle-up");
        fallback.put("circle-blank", "circle-o");
        fallback.put("cny", "rub");
        fallback.put("collapse-alt", "minus-square-o");
        fallback.put("collapse-top", "caret-square-o-up");
        fallback.put("collapse", "caret-square-o-down");
        fallback.put("comment-alt", "comment-o");
        fallback.put("comments-alt", "comments-o");
        fallback.put("copy", "files-o");
        fallback.put("cut", "scissors");
        fallback.put("dashboard", "tachometer");
        fallback.put("double-angle-down", "angle-double-down");
        fallback.put("double-angle-left", "angle-double-left");
        fallback.put("double-angle-right", "angle-double-right");
        fallback.put("double-angle-up", "angle-double-up");
        fallback.put("download", "arrow-circle-o-down");
        fallback.put("download-alt", "download");
        fallback.put("edit-sign", "pencil-square");
        fallback.put("edit", "pencil-square-o");
        fallback.put("ellipsis-horizontal", "ellipsis-h");
        fallback.put("ellipsis-vertical", "ellipsis-v");
        fallback.put("envelope-alt", "envelope-o");
        fallback.put("exclamation-sign", "exclamation-circle");
        fallback.put("expand-alt", "plus-square-o");
        fallback.put("expand", "caret-square-o-right");
        fallback.put("external-link-sign", "external-link-square");
        fallback.put("eye-close", "eye-slash");
        fallback.put("eye-open", "eye");
        fallback.put("facebook-sign", "facebook-square");
        fallback.put("facetime-video", "video-camera");
        fallback.put("file-alt", "file-o");
        fallback.put("file-text-alt", "file-text-o");
        fallback.put("flag-alt", "flag-o");
        fallback.put("folder-close-alt", "folder-o");
        fallback.put("folder-close", "folder");
        fallback.put("folder-open-alt", "folder-open-o");
        fallback.put("food", "cutlery");
        fallback.put("frown", "frown-o");
        fallback.put("fullscreen", "arrows-alt");
        fallback.put("github-sign", "github-square");
        fallback.put("google-plus-sign", "google-plus-square");
        fallback.put("group", "users");
        fallback.put("h-sign", "h-square");
        fallback.put("hand-down", "hand-o-down");
        fallback.put("hand-left", "hand-o-left");
        fallback.put("hand-right", "hand-o-right");
        fallback.put("hand-up", "hand-o-up");
        fallback.put("hdd", "hdd-o (4.0.1)");
        fallback.put("heart-empty", "heart-o");
        fallback.put("hospital", "hospital-o");
        fallback.put("indent-left", "outdent");
        fallback.put("indent-right", "indent");
        fallback.put("info-sign", "info-circle");
        fallback.put("keyboard", "keyboard-o");
        fallback.put("legal", "gavel");
        fallback.put("lemon", "lemon-o");
        fallback.put("lightbulb", "lightbulb-o");
        fallback.put("linkedin-sign", "linkedin-square");
        fallback.put("meh", "meh-o");
        fallback.put("microphone-off", "microphone-slash");
        fallback.put("minus-sign-alt", "minus-square");
        fallback.put("minus-sign", "minus-circle");
        fallback.put("mobile-phone", "mobile");
        fallback.put("moon", "moon-o");
        fallback.put("move", "arrows");
        fallback.put("off", "power-off");
        fallback.put("ok-circle", "check-circle-o");
        fallback.put("ok-sign", "check-circle");
        fallback.put("ok", "check");
        fallback.put("paper-clip", "paperclip");
        fallback.put("paste", "clipboard");
        fallback.put("phone-sign", "phone-square");
        fallback.put("picture", "picture-o");
        fallback.put("pinterest-sign", "pinterest-square");
        fallback.put("play-circle", "play-circle-o");
        fallback.put("play-sign", "play-circle");
        fallback.put("plus-sign-alt", "plus-square");
        fallback.put("plus-sign", "plus-circle");
        fallback.put("pushpin", "thumb-tack");
        fallback.put("question-sign", "question-circle");
        fallback.put("remove-circle", "times-circle-o");
        fallback.put("remove-sign", "times-circle");
        fallback.put("remove", "times");
        fallback.put("reorder", "bars");
        fallback.put("resize-full", "expand");
        fallback.put("resize-horizontal", "arrows-h");
        fallback.put("resize-small", "compress");
        fallback.put("resize-vertical", "arrows-v");
        fallback.put("rss-sign", "rss-square");
        fallback.put("save", "floppy-o");
        fallback.put("screenshot", "crosshairs");
        fallback.put("share-alt", "share");
        fallback.put("share-sign", "share-square");
        fallback.put("share", "share-square-o");
        fallback.put("sign-blank", "square");
        fallback.put("signin", "sign-in");
        fallback.put("signout", "sign-out");
        fallback.put("smile", "smile-o");
        fallback.put("sort-by-alphabet-alt", "sort-alpha-desc");
        fallback.put("sort-by-alphabet", "sort-alpha-asc");
        fallback.put("sort-by-attributes-alt", "sort-amount-desc");
        fallback.put("sort-by-attributes", "sort-amount-asc");
        fallback.put("sort-by-order-alt", "sort-numeric-desc");
        fallback.put("sort-by-order", "sort-numeric-asc");
        fallback.put("sort-down", "sort-desc");
        fallback.put("sort-up", "sort-asc");
        fallback.put("stackexchange", "stack-overflow");
        fallback.put("star-empty", "star-o");
        fallback.put("star-half-empty", "star-half-o");
        fallback.put("sun", "sun-o");
        fallback.put("thumbs-down-alt", "thumbs-o-down");
        fallback.put("thumbs-up-alt", "thumbs-o-up");
        fallback.put("time", "clock-o");
        fallback.put("trash", "trash-o");
        fallback.put("tumblr-sign", "tumblr-square");
        fallback.put("twitter-sign", "twitter-square");
        fallback.put("unlink", "chain-broken");
        fallback.put("upload", "arrow-circle-o-up");
        fallback.put("upload-alt", "upload");
        fallback.put("warning-sign", "exclamation-triangle");
        fallback.put("xing-sign", "xing-square");
        fallback.put("youtube-sign", "youtube-square");
        fallback.put("zoom-in", "search-plus");
        fallback.put("zoom-out", "search-minus");
    }

    // for a complete list: http://fortawesome.github.io/Font-Awesome/icons/
    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String                     icon;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String                     size;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private IconEffect                 effect;

    @Inject
    private ComponentResources         resources;

    @SetupRender
    void render(MarkupWriter writer) {

        String _icon = mapIcon();

        writer.element("i", "class", "fa fa-" + _icon);

        if (InternalUtils.isNonBlank(this.size)) {
            writer.attributes("class", "fa-" + this.size);
        }

        if (this.effect != null) {
            writer.attributes("class", "fa-" + this.effect.value);
        }

        this.resources.renderInformalParameters(writer);

        writer.end();
    }

    private String mapIcon() {

        if (this.icon.startsWith("icon-")) {

            String former = this.icon.substring(5);
            String recent = fallback.get(former);

            return recent != null ? recent : former;
        }

        return this.icon;

    }
}
