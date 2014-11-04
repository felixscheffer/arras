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

package com.github.fscheffer.arras.cms.pages;

import javax.inject.Inject;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.json.JSONObject;

import com.github.fscheffer.arras.cms.BlockContext;
import com.github.fscheffer.arras.cms.Content;

public class DefaultContentBlocks {

    @Environmental
    private BlockContext context;

    @Inject
    @Path("man_point-arena-stornetta.jpg")
    private Asset        defaultImage;

    @Content(value = "literal:<h1>A Great Headline</h1>")
    @Property
    private String       h1;

    @Content(value = "literal:<h2>A good subtitle</h2>")
    @Property
    private String       h2;

    @Content(value = "literal:<h3>Awesome feature</h3>")
    @Property
    private String       h3;

    @Content(contentId = "content", value = "literal:<p><b>Great stories have personality.</b> Consider telling a great story that provides personality. Writing a story with personality for potential clients will asist with making a relationship connection. This shows up in small quirks like word choices or phrases. Write from your point of view, not from someone else's experience.</p><p><b>Great stories are for everyone even when only written for just one person.</b> If you try to write with a wide general audience in mind, your story will ring false and be bland. No one will be interested. Write for one person. If it’s genuine for the one, it’s genuine for the rest.</p>")
    @Property
    private String       articleContent;

    @Content(contentId = "content", value = "literal:<p><b>Watch \"Jeopardy!\"</b>, Alex Trebek's fun TV quiz game. Woven silk pyjamas exchanged for blue quartz. Brawny gods just flocked up to quiz and vex him. Adjusting quiver and bow, Zompyc killed the fox. My faxed joke won a pager in the cable TV quiz show. <i>Amazingly</i> few discotheques provide jukeboxes. </p>")
    @Property
    private String       featureContent;

    @Content(contentId = "content", value = "literal:<p>Cras justo odio, dapibus ac facilisis in, egestas eget quam. Donec id elit non mi porta gravida at eget metus. Nullam id dolor id nibh ultricies vehicula ut id elit.</p>")
    @Property
    private String       teaserContent;

    @Content(value = "asset:man_point-arena-stornetta.jpg")
    @Property
    private String       image;

    public JSONObject getData() {
        return this.context.data;
    }
}
