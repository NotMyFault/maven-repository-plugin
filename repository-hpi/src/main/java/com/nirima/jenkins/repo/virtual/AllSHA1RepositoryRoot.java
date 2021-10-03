/*
 * The MIT License
 *
 * Copyright (c) 2011, Nigel Magnay / NiRiMa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nirima.jenkins.repo.virtual;

import com.nirima.jenkins.repo.RepositoryDirectory;
import com.nirima.jenkins.repo.RepositoryElement;
import com.nirima.jenkins.repo.build.DirectoryRepositoryItem;
import com.nirima.jenkins.repo.build.PopulateOnDemandDirectoryRepositoryItem;
import com.nirima.jenkins.repo.build.ProjectBuildRepositoryRoot;
import com.nirima.jenkins.repo.util.HudsonVisitor;
import com.nirima.jenkins.repo.util.HudsonWalker;
import com.nirima.jenkins.repo.util.IDirectoryPopulator;

import hudson.maven.MavenModuleSetBuild;
import hudson.model.Result;
import hudson.plugins.git.util.BuildData;

/**
 * Represent all projects by SHA1.
 */
public class AllSHA1RepositoryRoot extends PopulateOnDemandDirectoryRepositoryItem {

    public AllSHA1RepositoryRoot(RepositoryDirectory parent) {
        super(parent, "SHA1");
    }

     protected IDirectoryPopulator getPopulator() {
       return new IDirectoryPopulator() {
            public void populate(final DirectoryRepositoryItem directory) {
                HudsonWalker.traverseProjectsAndBuilds(new HudsonVisitor()
                {
                    public void visitModuleSet(MavenModuleSetBuild run)
                    {
                        BuildData bd = run.getAction(BuildData.class);
                        if (bd != null && run.getResult() == Result.SUCCESS) {
                            String sha1 = bd.getLastBuiltRevision().getSha1String();
                            RepositoryElement content = new ProjectBuildRepositoryRoot(AllSHA1RepositoryRoot.this,run,sha1);
                            add(content, false);

                        }
                    }
                });
            }
        };
    }
}
