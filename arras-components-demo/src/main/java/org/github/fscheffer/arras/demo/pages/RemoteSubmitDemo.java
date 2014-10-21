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

package org.github.fscheffer.arras.demo.pages;

import javax.inject.Inject;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class RemoteSubmitDemo {

    @Inject
    private AlertManager alertManager;

    @Persist(PersistenceConstants.FLASH)
    @Property
    private String       dummy1, dummy2;

    @OnEvent(value = EventConstants.SUCCESS, component = "formWithVisibleButton")
    public void onSuccessFromVisibleButton() {
        this.alertManager.success("Triggered form with visible submit button! Form content was: \"" + this.dummy1
                                  + "\"");
    }

    @OnEvent(value = EventConstants.SUCCESS, component = "formWithInvisibleButton")
    public void onSuccessFromInvisibleButton() {
        this.alertManager.success("Triggered form with invisible submit button! Form content was: \"" + this.dummy2
                                  + "\"");
    }
}
