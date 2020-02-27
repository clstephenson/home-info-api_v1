package com.clstephenson.homeinfo.api.auditing;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // todo change this to current user when security is added.
        return Optional.ofNullable("Chris");
    }

}
