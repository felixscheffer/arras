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

package org.github.fscheffer.arras.cms.demo.pages;

import javax.inject.Inject;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.jpa.annotations.CommitAfter;
import org.apache.tapestry5.json.JSONObject;
import org.github.fscheffer.arras.ArrasUtils;
import org.github.fscheffer.arras.cms.ArrasCmsConstants;
import org.github.fscheffer.arras.cms.services.PageContentDao;
import org.github.fscheffer.arras.cms.services.PermissionManager;
import org.slf4j.Logger;

public class Simple {

    @Inject
    private Logger            logger;

    @Inject
    private PageContentDao    contentDao;

    @Inject
    private PermissionManager permissionManager;

    @Inject
    private AlertManager      alerts;

    private JSONObject        data;

    @OnEvent(EventConstants.ACTIVATE)
    void onActivate() {
        this.data = this.contentDao.getContentAsObject("toplevel");
    }

    @CommitAfter
    @OnEvent(ArrasCmsConstants.SUBMIT_CONTENT)
    void onSucess() {

        if (!this.permissionManager.hasPermissionToEdit()) {

            this.logger.info("User has no permission to edit page '{}'!", Simple.class.getName());

            this.alerts.error("User has no permission to edit page '" + Simple.class.getName() + "'!");

            return;
        }

        this.contentDao.save("toplevel", this.data);

        this.logger.info("Changes saved using ajax!");

        this.alerts.success("Changes saved!");
    }

    public boolean isEditing() {
        return this.permissionManager.hasPermissionToEdit();
    }

    public String getH1() {
        return ArrasUtils.get(this.data, "h1", "<h1>A Great Headline</h1>");
    }

    public void setH1(String value) {
        this.data.put("h1", value);
    }

    public String getH2() {
        return ArrasUtils.get(this.data, "h2", "<h2>A good subtitle</h2>");
    }

    public void setH2(String value) {
        this.data.put("h2", value);
    }

    public String getSubtitel() {
        return ArrasUtils.get(this.data,
                              "subtitel",
            "<p>A great way to catch your reader's attention is to tell a story. Everything you consider writing can be told as a story.</p>");
    }

    public void setSubtitel(String value) {
        this.data.put("subtitel", value);
    }

    public String getContent() {
        return ArrasUtils.get(this.data,
                              "content",
                              "<p>But as the junior mates were hurrying to execute the order, a pale man, with a bandaged head, arrested them—<b>Radney</b> the <i>chief mate</i>. Ever since the blow, he had lain in his berth; but that morning, hearing the tumult on the deck, he had crept out, and thus far had watched the whole scene. Such was the state of his mouth, that he could hardly speak; but mumbling something about his being willing and able to do what the captain dared not attempt, he snatched the rope and advanced to his pinioned foe.</p>"
                                  + "<p>In his treatise on <b>\"Queen-Gold,\"</b> or Queen-pinmoney, an old King's Bench author, one William Prynne, thus discourseth: \"Ye tail is ye Queen's, that ye Queen's wardrobe may be supplied with ye whalebone.\" Now this was written at a time when the <i>black limber bone of the Greenland or Right whale</i> was largely used in ladies' bodices. But this same bone is not in the tail; it is in the head, which is a sad mistake for a sagacious lawyer like Prynne. But is the Queen a mermaid, to be presented with a tail? An allegorical meaning may lurk here.</p>"
                                  + "<p>There are two royal fish so styled by the English law writers—the whale and the sturgeon; both royal property under certain limitations, and nominally supplying the tenth branch of the crown's ordinary revenue. I know not that any other author has hinted of the matter; but by inference it seems to me that the sturgeon must be divided in the same way as the whale, the King receiving the highly dense and elastic head peculiar to that fish, which, symbolically regarded, may possibly be humorously grounded upon some presumed congeniality. And thus there seems a reason in all things, even in law.</p>");
    }

    public void setContent(String value) {
        this.data.put("content", value);
    }

    @Inject
    @Path("photos/landscape/man_point-arena-stornetta.jpg")
    private Asset defaultImage;

    public String getImage() {
        return ArrasUtils.get(this.data, "image", this.defaultImage.toClientURL());
    }

    public void setImage(String newValue) {
        this.data.put("image", newValue);
    }
}
