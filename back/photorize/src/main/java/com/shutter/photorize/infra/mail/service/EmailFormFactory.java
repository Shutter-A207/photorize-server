package com.shutter.photorize.infra.mail.service;

import com.shutter.photorize.infra.mail.model.EmailForm;
import com.shutter.photorize.infra.mail.model.EmailFormData;

public interface EmailFormFactory {

	EmailForm createEmailForm(String to, boolean isHtml, EmailFormData emailFormData);
}
