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

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.jpa.annotations.CommitAfter;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.github.fscheffer.arras.ArrasUtils;
import org.github.fscheffer.arras.cms.ArrasCmsConstants;
import org.github.fscheffer.arras.cms.components.ContentContainer;
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

        JSONArray array = this.contentDao.getContent("toplevel");

        this.data = array.length() == 0 ? new JSONObject() : array.getJSONObject(0);
    }

    @InjectComponent
    private ContentContainer container;

    @CommitAfter
    @OnEvent(ArrasCmsConstants.SUBMIT_CONTENT)
    void onSucess() {

        if (!this.permissionManager.hasPermissionToEdit()) {

            this.logger.info("User has no permission to edit page '{}'!", Simple.class.getName());

            this.alerts.error("User has no permission to edit page '" + Simple.class.getName() + "'!");
        }

        this.contentDao.save("toplevel", new JSONArray(this.data));

        this.logger.info("Changes saved using ajax!");

        this.alerts.success("Changes saved!");
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
            "<p><b>Great stories have personality.</b> Consider telling a great story that provides personality. Writing a story with personality for potential clients will asist with making a relationship connection. This shows up in small quirks like word choices or phrases. Write from your point of view, not from someone else's experience.</p><p><b>Great stories are for everyone even when only written for just one person.</b> If you try to write with a wide general audience in mind, your story will ring false and be bland. No one will be interested. Write for one person. If it’s genuine for the one, it’s genuine for the rest.</p>");
    }

    public void setContent(String value) {
        this.data.put("content", value);
    }
}
