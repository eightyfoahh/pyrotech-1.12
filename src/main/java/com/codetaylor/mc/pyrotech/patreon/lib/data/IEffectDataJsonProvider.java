package com.codetaylor.mc.pyrotech.patreon.lib.data;

import java.io.Reader;
import java.util.Optional;

public interface IEffectDataJsonProvider {

  Optional<Reader> getEffectDataJson();
}
