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
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.jpa.annotations.CommitAfter;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.github.fscheffer.arras.ArrasUtils;
import org.github.fscheffer.arras.cms.ArrasCmsConstants;
import org.github.fscheffer.arras.cms.services.PageContentDao;
import org.github.fscheffer.arras.cms.services.PermissionManager;
import org.slf4j.Logger;

public class Index {

    @Inject
    private AlertManager       alertManager;

    @Inject
    private ComponentResources resources;

    @Inject
    private Logger             logger;

    @Inject
    private PermissionManager  permissionManager;

    @Inject
    private PageContentDao     contentDao;

    @Inject
    @Path("photos/landscape/man_point-arena-stornetta.jpg")
    private Asset              defaultImage;

    private JSONObject         data;

    @OnEvent(EventConstants.ACTIVATE)
    void onActivate() {

        JSONArray array = this.contentDao.getContent("toplevel");

        this.data = array.length() == 0 ? new JSONObject() : array.getJSONObject(0);
    }

    @CommitAfter
    @OnEvent(ArrasCmsConstants.SUBMIT_CONTENT)
    void onSubmitContent() {

        String pageName = this.resources.getPageName();

        if (!this.permissionManager.hasPermissionToEdit()) {

            this.logger.info("User has no permission to edit page '{}'!", pageName);
            return;
        }

        this.contentDao.save("toplevel", new JSONArray(this.data));

        this.logger.info("Changes saved using ajax!");
        this.alertManager.success("Changes saved using ajax!");
    }

    public String getFeatureH1() {
        return ArrasUtils.get(this.data, "featureH1", "<h1>Features</h1>");
    }

    public String getMoreFeatureH1() {
        return ArrasUtils.get(this.data, "moreFeatureH1", "<h1>More features</h1>");
    }

    public void setFeatureH1(String value) {
        this.data.put("featureH1", value);
    }

    public void setMoreFeatureH1(String value) {
        this.data.put("moreFeatureH1", value);
    }

    public String getImageUrl() {
        return ArrasUtils.get(this.data, "image", this.defaultImage.toClientURL());
    }

    public void setImageUrl(String value) {
        this.data.put("image", value);
    }
}
