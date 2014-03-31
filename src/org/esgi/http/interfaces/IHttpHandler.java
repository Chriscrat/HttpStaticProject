package org.esgi.http.interfaces;

import java.io.IOException;

public interface IHttpHandler {
	void execute(IRequestHttpHandler request, IResponseHttpHandler response) throws IOException;
}
