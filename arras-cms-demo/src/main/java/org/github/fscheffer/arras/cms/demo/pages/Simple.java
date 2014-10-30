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

import java.util.Locale;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.jpa.annotations.CommitAfter;
import org.github.fscheffer.arras.cms.entities.PageContent;
import org.github.fscheffer.arras.cms.entities.PageContentId;
import org.github.fscheffer.arras.cms.services.PageContentDao;

public class Simple {

    @Inject
    private AlertManager       alertManager;

    @Inject
    private Locale             locale;

    @Inject
    private ComponentResources resources;

    @Inject
    private PageContentDao     contentDao;

    @CommitAfter
    @OnEvent(value = EventConstants.SUCCESS, component = "container")
    void onSucess() {
        this.alertManager.success("Changes saved!");
    }

    public PageContent getH1() {
        return loadContent("h1", "<h1>A Great Headline</h1>");
    }

    public PageContent getH2() {
        return loadContent("h2", "<h2>A good subtitle</h2>");
    }

    public PageContent getSubtitel() {
        return loadContent("subtitel",
                           "<p>A great way to catch your reader's attention is to tell a story. Everything you consider writing can be told as a story.</p>");
    }

    public PageContent getContent() {
        return loadContent("content",
                           "<p><b>Great stories have personality.</b> Consider telling a great story that provides personality. Writing a story with personality for potential clients will asist with making a relationship connection. This shows up in small quirks like word choices or phrases. Write from your point of view, not from someone else's experience.</p><p><b>Great stories are for everyone even when only written for just one person.</b> If you try to write with a wide general audience in mind, your story will ring false and be bland. No one will be interested. Write for one person. If it’s genuine for the one, it’s genuine for the rest.</p>");
    }

    private PageContent loadContent(String contentId, String defaultValue) {

        String pageName = this.resources.getPageName();

        PageContentId id = new PageContentId(pageName, this.locale, contentId, 0);

        return this.contentDao.getContent(id, defaultValue);
    }
}
