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

import java.util.Date;

import javax.inject.Inject;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.github.fscheffer.arras.components.LightboxContent;

public class LightboxDemo {

    @InjectComponent
    private LightboxContent content, contentWithinZone;

    @Inject
    private Block           lightboxContent, zoneContent;

    public String getContentId() {
        return "#" + this.content.getClientId();
    }

    public String getContentWithinZoneId() {
        return "#" + this.contentWithinZone.getClientId();
    }

    public Date getDate() {
        return new Date();
    }

    @OnEvent("someAjaxEvent")
    public Block onUpdateZone() {
        return this.lightboxContent;
    }

    @OnEvent("triggerZone")
    public Block onTriggerZone() {
        return this.zoneContent;
    }

}
