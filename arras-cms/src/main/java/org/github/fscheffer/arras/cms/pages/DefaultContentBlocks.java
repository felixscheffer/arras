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

package org.github.fscheffer.arras.cms.pages;

import javax.inject.Inject;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Path;
import org.github.fscheffer.arras.ArrasUtils;
import org.github.fscheffer.arras.cms.BlockContext;

public class DefaultContentBlocks {

    @Environmental
    private BlockContext context;

    @Inject
    @Path("man_point-arena-stornetta.jpg")
    private Asset        defaultImage;

    private String get(String property, String defaultValue) {
        return ArrasUtils.get(this.context.data, property, defaultValue);
    }

    private void set(String key, String value) {
        this.context.data.put(key, value);
    }

    public String getH1() {
        return get("h1", "<h1>A Great Headline</h1>");
    }

    public String getH2() {
        return get("h2", "<h2>A good subtitle</h2>");
    }

    public String getH3() {
        return get("h3", "<h3>Awesome feature</h3>");
    }

    public String getArticleContent() {
        return get("content",
            "<p><b>Great stories have personality.</b> Consider telling a great story that provides personality. Writing a story with personality for potential clients will asist with making a relationship connection. This shows up in small quirks like word choices or phrases. Write from your point of view, not from someone else's experience.</p><p><b>Great stories are for everyone even when only written for just one person.</b> If you try to write with a wide general audience in mind, your story will ring false and be bland. No one will be interested. Write for one person. If it’s genuine for the one, it’s genuine for the rest.</p>");
    }

    public String getFeatureContent() {
        return get("content",
            "<p><b>Watch \"Jeopardy!\"</b>, Alex Trebek's fun TV quiz game. Woven silk pyjamas exchanged for blue quartz. Brawny gods just flocked up to quiz and vex him. Adjusting quiver and bow, Zompyc killed the fox. My faxed joke won a pager in the cable TV quiz show. <i>Amazingly</i> few discotheques provide jukeboxes. </p>");
    }

    public void setH1(String value) {
        set("h1", value);
    }

    public void setH2(String value) {
        set("h2", value);
    }

    public void setH3(String value) {
        set("h3", value);
    }

    public void setArticleContent(String value) {
        set("content", value);
    }

    public void setFeatureContent(String value) {
        set("content", value);
    }

    public String getTeaserContent() {
        return get("teaserContent",
            "<p>Cras justo odio, dapibus ac facilisis in, egestas eget quam. Donec id elit non mi porta gravida at eget metus. Nullam id dolor id nibh ultricies vehicula ut id elit.</p>");
    }

    public void setTeaserContent(String value) {
        set("teaserContent", value);
    }

    public String getImage() {
        return get("image", this.defaultImage.toClientURL());
    }

    public void setImage(String value) {
        set("image", value);
    }
}
