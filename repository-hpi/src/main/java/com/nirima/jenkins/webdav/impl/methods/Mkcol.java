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
package com.nirima.jenkins.webdav.impl.methods;


import com.nirima.jenkins.webdav.interfaces.IDavCollection;
import com.nirima.jenkins.webdav.interfaces.IDavContext;
import com.nirima.jenkins.webdav.interfaces.IDavItem;
import com.nirima.jenkins.webdav.interfaces.IDavRepo;
import com.nirima.jenkins.webdav.interfaces.MethodException;

import javax.servlet.http.HttpServletResponse;

/**
 * @author nigelm
 */
public class Mkcol extends MethodBase {

    /*
     * (non-Javadoc)
     * 
     * @see nrm.webdav.interfaces.IMethod#invoke()
     */
    @Override
    public void invoke(IDavContext ctxt) throws MethodException {

        try {
            IDavRepo repo = getRepo();
            boolean created = false;

            String path = this.getPath();
            if (path.endsWith("/")) path = path.substring(0, path.length() - 1);

            IDavItem item = repo.getItem(getDavContext(), this.getPath());

            if (item == null) {
                // The item couldn't be found, so it is a creation.
                // Make the item in the parent path.
                int lastSlash = path.lastIndexOf("/");
                String parent = path.substring(0, lastSlash);
                IDavCollection parentFolder = (IDavCollection) repo.getItem(getDavContext(), parent);

                parentFolder.createCollection(getDavContext(), path.substring(lastSlash + 1));

                created = true;
            }

            if (created)
                this.getResponse().setStatus(HttpServletResponse.SC_CREATED);
            else
                this.getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (Exception e) {
            throw new MethodException("Error creating collection", e);
        }

    }

}
