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
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.jpa.annotations.CommitAfter;
import org.apache.tapestry5.json.JSONObject;
import org.github.fscheffer.arras.cms.ArrasCmsConstants;
import org.github.fscheffer.arras.cms.services.PageContentDao;
import org.github.fscheffer.arras.cms.services.PermissionManager;
import org.slf4j.Logger;

public class Blocks {

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

}
