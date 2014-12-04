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

package com.github.fscheffer.arras.demo.pages;

import javax.inject.Inject;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Path;

import com.github.fscheffer.arras.PlayerSource;
import com.github.fscheffer.arras.PlayerSourceImpl;

public class PlayerDemo {

    @Inject
    @Path("Miaow-07-Bubble.m4a")
    private Asset miaowM4a;

    @Inject
    @Path("Miaow-07-Bubble.ogg")
    private Asset miaowOgg;

    @Inject
    @Path("Big_Buck_Bunny_Trailer.m4v")
    private Asset bigBuckBunnyM4v;

    @Inject
    @Path("Big_Buck_Bunny_Trailer.ogv")
    private Asset bigBuckBunnyOgv;

    public PlayerSource getAudio() {

        PlayerSource source = new PlayerSourceImpl();
        source.add("audio/ogg", this.miaowOgg.toClientURL());
        source.add("audio/mp4", this.miaowM4a.toClientURL());
        return source;
    }

    public PlayerSource getVideo() {

        PlayerSource source = new PlayerSourceImpl();
        source.add("video/m4v", this.bigBuckBunnyM4v.toClientURL());
        source.add("video/ogg", this.bigBuckBunnyOgv.toClientURL());
        return source;
    }
}
