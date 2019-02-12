package com.codetaylor.mc.pyrotech.modules.storage.tile;

import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorageConfig;

public class TileBagSimple
    extends TileBagBase {

  @Override
  public int getItemCapacity() {

    return ModuleStorageConfig.SIMPLE_ROCK_BAG.MAX_ITEM_CAPACITY;
  }

  @Override
  protected String[] getAllowedItemStrings() {

    return ModuleStorageConfig.SIMPLE_ROCK_BAG.ALLOWED_ITEMS;
  }

  @Override
  public boolean allowAutomation() {

    return ModuleStorageConfig.SIMPLE_ROCK_BAG.ALLOW_AUTOMATION;
  }
}
