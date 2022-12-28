package de.lunarakai.lunaauction.utils.playerinteraction;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import de.lunarakai.lunaauction.LunaAuction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.*;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.json.simple.JSONArray;

import java.util.*;

public interface ItemUtil {

    String[] BYPASS_CLASS = {"CraftMetaBlockState", "CraftMetaItem"
            /*Glowstone Support*/, "GlowMetaItem"};

    static String itemToJson(ItemStack item) {

        Gson gson = new Gson();
        JsonObject itemJson = new JsonObject();

        itemJson.addProperty("type", item.getType().name());
        if (item.getDurability() > 0) {
            itemJson.addProperty("data", item.getDurability());
        }
        if (item.getAmount() != 1) {
            itemJson.addProperty("amount", item.getAmount());
        }
        if (item.hasItemMeta()) {
            JsonObject metaJson = new JsonObject();
            ItemMeta meta = item.getItemMeta();

            if (meta.hasDisplayName()) {
                if(item.displayName().equals(Component.translatable("block.minecraft.ominous_banner"))) {
                    metaJson.addProperty("displayname",item.translationKey());
                } else {
                    LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.builder().build();
                    metaJson.addProperty("displayname", legacyComponentSerializer.serialize(item.displayName()));
                }

            }
            if (meta.hasLore()) {
                JsonArray lore = new JsonArray();
                meta.getLore().forEach(str -> lore.add(new JsonPrimitive(str)));
                metaJson.add("lore", lore);
            }
            if (meta.hasEnchants()) {
                JsonArray enchants = new JsonArray();
                meta.getEnchants().forEach((enchantment, integer) -> {
                    NamespacedKey key = enchantment.getKey();
                    String stringKey = key.toString().replace(key.namespace() + ":","");
                    enchants.add(new JsonPrimitive(stringKey + ":" + integer));
                });
                metaJson.add("enchants", enchants);
            }
            if (!meta.getItemFlags().isEmpty()) {
                JsonArray flags = new JsonArray();
                meta.getItemFlags().stream().map(ItemFlag::name).forEach(str -> flags.add(new JsonPrimitive(str)));
            }

            for (String clazz : BYPASS_CLASS) {
                if (meta.getClass().getSimpleName().equals(clazz) && !(item.getType().equals(Material.SHIELD))) {

                    itemJson.add("item-meta", metaJson);
                    return gson.toJson(itemJson);
                } else {
                    break;
                }
            }

            if (meta instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta) meta;
                if (skullMeta.hasOwner()) {
                    JsonObject extrMeta = new JsonObject();
                    extrMeta.addProperty("owner", skullMeta.getOwner());
                    metaJson.add("extra-meta", extrMeta);
                }
            } else if(item.getType().equals(Material.SHIELD)) {
                BlockStateMeta  bmeta = (BlockStateMeta) meta;
                JsonObject extraMeta = new JsonObject();
                Banner banner = (Banner) bmeta.getBlockState();

                extraMeta.addProperty("color", banner.getBaseColor().toString());

                if (banner.numberOfPatterns() > 0) {
                    JsonArray patterns = new JsonArray();
                    banner.getPatterns()
                                .stream()
                                .map(pattern ->
                                        pattern.getColor().name() + ":" + pattern.getPattern().getIdentifier())
                                .forEach(str -> patterns.add(new JsonPrimitive(str)));
                        extraMeta.add("patterns", patterns);
                }
                metaJson.add("extra-meta", extraMeta);
            }else if (meta instanceof BannerMeta) {
                BannerMeta bannerMeta = (BannerMeta) meta;
                JsonObject extraMeta = new JsonObject();
                //extraMeta.addProperty("base-color", bannerMeta.getBaseColor().name());

                if (bannerMeta.numberOfPatterns() > 0) {
                    JsonArray patterns = new JsonArray();
                    bannerMeta.getPatterns()
                            .stream()
                            .map(pattern ->
                                    pattern.getColor().name() + ":" + pattern.getPattern().getIdentifier())
                            .forEach(str -> patterns.add(new JsonPrimitive(str)));
                    extraMeta.add("patterns", patterns);
                }
                metaJson.add("extra-meta", extraMeta);
            } else if (meta instanceof EnchantmentStorageMeta) {
                EnchantmentStorageMeta esMeta = (EnchantmentStorageMeta) meta;
                if (esMeta.hasStoredEnchants()) {
                    JsonObject extraMeta = new JsonObject();
                    JsonArray storedEnchants = new JsonArray();
                    esMeta.getStoredEnchants().forEach(((enchantment, integer) -> {
                        NamespacedKey key = enchantment.getKey();
                        String stringKey = key.toString().replace(key.namespace() + ":", "");
                        storedEnchants.add(new JsonPrimitive(stringKey + ":" + integer));
                    }));
                    extraMeta.add("stored-enchants", storedEnchants);
                    metaJson.add("extra-meta", extraMeta);
                }
            } else if (meta instanceof LeatherArmorMeta) {
                LeatherArmorMeta lemeta = (LeatherArmorMeta) meta;
                JsonObject extraMeta = new JsonObject();
                extraMeta.addProperty("color", Integer.toHexString(lemeta.getColor().asRGB()));
                metaJson.add("extra-meta", extraMeta);
            } else if (meta instanceof BookMeta) {
                BookMeta bookMeta = (BookMeta) meta;
                if (bookMeta.hasAuthor() || bookMeta.hasPages() || bookMeta.hasTitle()) {
                    JsonObject extraMeta = new JsonObject();
                    if (bookMeta.hasTitle()) {
                        extraMeta.addProperty("title", bookMeta.getTitle());
                    }
                    if (bookMeta.hasAuthor()) {
                        extraMeta.addProperty("author", bookMeta.getAuthor());
                    }
                    if (bookMeta.hasPages()) {
                        JsonArray pages = new JsonArray();
                        bookMeta.getPages().forEach(str -> pages.add(new JsonPrimitive(str)));
                        extraMeta.add("pages", pages);
                    }
                    metaJson.add("extra-meta", extraMeta);
                }
            } else if (meta instanceof PotionMeta) {
                PotionMeta pmeta = (PotionMeta) meta;
                JsonObject extraMeta = new JsonObject();
                if (pmeta.hasCustomEffects()) {
                    JsonArray customEffects = new JsonArray();
                    pmeta.getCustomEffects().forEach(potionEffect -> {
                        customEffects.add(new JsonPrimitive(potionEffect.getType().getName()
                                + ":" + potionEffect.getAmplifier()
                                + ":" + potionEffect.getDuration() / 20));
                    });
                    extraMeta.add("custom-effects", customEffects);
                } else {
                    PotionType type = pmeta.getBasePotionData().getType();
                    boolean isExtended = pmeta.getBasePotionData().isExtended();
                    boolean isUpgraded = pmeta.getBasePotionData().isUpgraded();
                    JsonObject baseEffect = new JsonObject();
                    baseEffect.addProperty("type", type.getEffectType().getName());
                    baseEffect.addProperty("isExtended", isExtended);
                    baseEffect.addProperty("isUpgraded", isUpgraded);
                    extraMeta.add("base-effect", baseEffect);
                }
                metaJson.add("extra-meta", extraMeta);
            }  else if (meta instanceof FireworkEffectMeta) {
                FireworkEffectMeta fireworkEffectMeta = (FireworkEffectMeta) meta;
                if (fireworkEffectMeta.hasEffect()) {
                    FireworkEffect effectMeta = fireworkEffectMeta.getEffect();
                    JsonObject extraMeta = new JsonObject();

                    extraMeta.addProperty("type", effectMeta.getType().name());
                    if (effectMeta.hasFlicker()) extraMeta.addProperty("flicker", true);
                    if (effectMeta.hasTrail()) extraMeta.addProperty("trail", true);

                    if (!effectMeta.getColors().isEmpty()) {
                        JsonArray colors = new JsonArray();
                        effectMeta.getColors().forEach(color ->
                                colors.add(new JsonPrimitive(Integer.toHexString(color.asRGB()))));
                        extraMeta.add("colors", colors);
                    }

                    if (!effectMeta.getFadeColors().isEmpty()) {
                        JsonArray fadeColors = new JsonArray();
                        effectMeta.getFadeColors().forEach(color ->
                                fadeColors.add(new JsonPrimitive(Integer.toHexString(color.asRGB()))));
                        extraMeta.add("fade-colors", fadeColors);
                    }

                    metaJson.add("extra-meta", extraMeta);
                }
            } else if (meta instanceof FireworkMeta) {
                FireworkMeta fireworkMeta = (FireworkMeta) meta;

                JsonObject extraMeta = new JsonObject();

                extraMeta.addProperty("power", fireworkMeta.getPower());

                if (fireworkMeta.hasEffects()) {
                    JsonArray effects = new JsonArray();
                    fireworkMeta.getEffects().forEach(effect -> {
                        JsonObject jsonObject = new JsonObject();

                        jsonObject.addProperty("type", effect.getType().name());
                        if (effect.hasFlicker()) jsonObject.addProperty("flicker", true);
                        if (effect.hasTrail()) jsonObject.addProperty("trail", true);
                        ;

                        if (!effect.getColors().isEmpty()) {
                            JsonArray colors = new JsonArray();
                            effect.getColors().forEach(color ->
                                    colors.add(new JsonPrimitive(Integer.toHexString(color.asRGB()))));
                            jsonObject.add("colors", colors);
                        }

                        if (!effect.getFadeColors().isEmpty()) {
                            JsonArray fadeColors = new JsonArray();
                            effect.getFadeColors().forEach(color ->
                                    fadeColors.add(new JsonPrimitive(Integer.toHexString(color.asRGB()))));
                            jsonObject.add("fade-colors", fadeColors);
                        }

                        effects.add(jsonObject);
                    });
                    extraMeta.add("effects", effects);
                }
                metaJson.add("extra-meta", extraMeta);
            } else if (meta instanceof MapMeta) {
                MapMeta mapMeta = (MapMeta) meta;
                JsonObject extraMeta = new JsonObject();

                if(mapMeta.hasMapView()) {
                    JsonArray mapView = new JsonArray();
                    mapView.add(mapMeta.getMapView().getWorld().getUID().toString());
                    mapView.add(mapMeta.getMapView().getId());
                    mapView.add(mapMeta.getMapView().getCenterX());
                    mapView.add(mapMeta.getMapView().getCenterZ());
                    mapView.add(mapMeta.getMapView().getScale().toString());
                    extraMeta.add("map-view", mapView);
                }
                if(mapMeta.getMapView().getRenderers() != null) {

                    JsonArray rendererArray = null;
                    LunaAuction.LOGGER.info("Map has renderers o.O");
                }
                if (mapMeta.hasLocationName()) {
                    extraMeta.addProperty("location-name", mapMeta.getLocationName());
                }
                if (mapMeta.hasColor()) {
                    extraMeta.addProperty("color", Integer.toHexString(mapMeta.getColor().asRGB()));
                }

                extraMeta.addProperty("scaling", mapMeta.isScaling());

                metaJson.add("extra-meta", extraMeta);
            }
            itemJson.add("item-meta", metaJson);
        }
        return gson.toJson(itemJson);
    }

    static ItemStack jsonToItem(String serializedItem) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(serializedItem);
        if (element.isJsonObject()) {
            JsonObject itemJson = element.getAsJsonObject();

            JsonElement typeElement = itemJson.get("type");
            JsonElement dataElement = itemJson.get("data");
            JsonElement amountElement = itemJson.get("amount");

            ItemStack itemStack;
            if (typeElement.isJsonPrimitive()) {
                String type = typeElement.getAsString();
                short data = dataElement != null ? dataElement.getAsShort() : 0;
                int amount = amountElement != null ? amountElement.getAsInt() : 1;

                itemStack = new ItemStack(Material.getMaterial(type));
                itemStack.setDurability(data);
                itemStack.setAmount(amount);

                JsonElement itemMetaElement = itemJson.get("item-meta");
                ItemMeta meta;
                if (itemMetaElement != null && itemMetaElement.isJsonObject()) {
                    meta = itemStack.getItemMeta();
                    JsonObject metaJson = itemMetaElement.getAsJsonObject();

                    JsonElement displayNameElement = metaJson.get("displayname");
                    JsonElement loreElement = metaJson.get("lore");
                    JsonElement enchants = metaJson.get("enchants");
                    JsonElement flagsElement = metaJson.get("flags");
                    if (displayNameElement != null && displayNameElement.isJsonPrimitive()) {
                        //TODO: show text for TranslatableComponent (for example Omnious Banner) correctly

                        LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.legacyAmpersand();
                        /*
                           try{
                               if(displayNameElement.equals("block.minecraft.ominous_banner")) {
                               }
                           } catch(Exception e) {

                           }

                        if(displayNameElement.equals(Component.translatable("block.minecraft.ominous_banner"))) {
                            NamespacedKey key = NamespacedKey.minecraft("block.minecraft.ominous_banner");
                            meta.displayName() = key.value();
                        } else {

                        }

                        */
                        String displayString = displayNameElement.getAsString();
                        displayString = displayString.replaceAll("[\\[\\]]", "");
                        TextComponent displayName = legacyComponentSerializer.deserialize(displayString);

                        meta.displayName(displayName);
                    }
                    if (loreElement != null && loreElement.isJsonArray()) {
                        JsonArray jsonArray = loreElement.getAsJsonArray();
                        List<String> lore = new ArrayList<>(jsonArray.size());
                        jsonArray.forEach(jsonElement -> {
                            if (jsonElement.isJsonPrimitive()) lore.add(jsonElement.getAsString());
                        });
                        meta.setLore(lore);
                    }
                    if (enchants != null && enchants.isJsonArray()) {
                        JsonArray jarray = enchants.getAsJsonArray();
                        jarray.forEach(jsonElement -> {
                            if (jsonElement.isJsonPrimitive()) {
                                String enchantString = jsonElement.getAsString();
                                if (enchantString.contains(":")) {
                                    try {
                                        String[] splitEnchant = enchantString.split(":");
                                        meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(splitEnchant[0])), Integer.parseInt(splitEnchant[1]), true);
                                    } catch (NumberFormatException ex) {
                                        LunaAuction.LOGGER.warning("PAAAAAAAIN: " + ex);
                                    }
                                }
                            }
                        });
                    }

                    if (flagsElement != null && flagsElement.isJsonArray()) {
                        JsonArray jsonArray = flagsElement.getAsJsonArray();
                        jsonArray.forEach(jsonElement -> {
                            if (jsonElement.isJsonPrimitive()) {
                                for (ItemFlag flag : ItemFlag.values()) {
                                    if (flag.name().equalsIgnoreCase(jsonElement.getAsString())) {
                                        meta.addItemFlags(flag);
                                        break;
                                    }
                                }
                            }
                        });
                    }
                    for (String clazz : BYPASS_CLASS) {
                        if (meta.getClass().getSimpleName().equalsIgnoreCase(clazz) && !(itemStack.getType().equals(Material.SHIELD))) {
                            LunaAuction.LOGGER.info("Extra-meta is getting bypassed");
                            itemStack.setItemMeta(meta);
                            return itemStack;
                        } else {
                            break;
                        }
                    }

                    JsonElement extraMetaElement = metaJson.get("extra-meta");

                    if (extraMetaElement != null && extraMetaElement.isJsonObject()) {
                        try {
                            JsonObject extraJson = extraMetaElement.getAsJsonObject();
                            if (meta instanceof SkullMeta) {
                                JsonElement ownerElement = extraJson.get("owner");
                                if (ownerElement != null && ownerElement.isJsonPrimitive()) {
                                    SkullMeta skullMeta = (SkullMeta) meta;
                                    skullMeta.setOwner(ownerElement.getAsString());
                                }
                            } else if(itemStack.getType().equals(Material.SHIELD)) {
                                BlockStateMeta blockStateMeta = (BlockStateMeta) meta;
                                Banner banner = (Banner) blockStateMeta.getBlockState();
                                JsonElement patternsElement = extraJson.get("patterns");
                                JsonElement colorElement = extraJson.get("color");

                                banner.setBaseColor(DyeColor.valueOf(colorElement.getAsString()));

                                if (patternsElement != null && patternsElement.isJsonArray()) {
                                    JsonArray jarray = patternsElement.getAsJsonArray();
                                    List<Pattern> patterns = new ArrayList<>(jarray.size());
                                    jarray.forEach(jsonElement -> {
                                        String patternString = jsonElement.getAsString();
                                        if (patternString.contains(":")) {
                                            String[] splitPattern = patternString.split(":");
                                            Optional<DyeColor> color = Arrays.stream(DyeColor.values())
                                                    .filter(dyeColor -> dyeColor.name().equalsIgnoreCase(splitPattern[0]))
                                                    .findFirst();
                                            PatternType patternType = PatternType.getByIdentifier(splitPattern[1]);
                                            if (color.isPresent() && patternType != null) {
                                                patterns.add(new Pattern(color.get(), patternType));
                                            }
                                        }
                                    });
                                    if (!patterns.isEmpty()) banner.setPatterns(patterns);
                                    banner.update();
                                    blockStateMeta.setBlockState(banner);
                                }

                            } else if (meta instanceof BannerMeta) {
                                //JsonElement baseColorElement = extraJson.get("base-color");
                                JsonElement patternsElement = extraJson.get("patterns");
                                BannerMeta bannerMeta = (BannerMeta) meta;
                                if (patternsElement != null && patternsElement.isJsonArray()) {
                                    JsonArray jarray = patternsElement.getAsJsonArray();
                                    List<Pattern> patterns = new ArrayList<>(jarray.size());
                                    jarray.forEach(jsonElement -> {
                                        String patternString = jsonElement.getAsString();
                                        if (patternString.contains(":")) {
                                            String[] splitPattern = patternString.split(":");
                                            Optional<DyeColor> color = Arrays.stream(DyeColor.values())
                                                    .filter(dyeColor -> dyeColor.name().equalsIgnoreCase(splitPattern[0]))
                                                    .findFirst();
                                            PatternType patternType = PatternType.getByIdentifier(splitPattern[1]);
                                            if (color.isPresent() && patternType != null) {
                                                patterns.add(new Pattern(color.get(), patternType));
                                            }
                                        }
                                    });
                                    if (!patterns.isEmpty()) bannerMeta.setPatterns(patterns);
                                }
                            } else if (meta instanceof EnchantmentStorageMeta) {
                                JsonElement storedEnchantsElement = extraJson.get("stored-enchants");
                                if (storedEnchantsElement != null && storedEnchantsElement.isJsonArray()) {
                                    EnchantmentStorageMeta esmeta = (EnchantmentStorageMeta) meta;
                                    JsonArray jarray = storedEnchantsElement.getAsJsonArray();
                                    jarray.forEach(jsonElement -> {
                                        if (jsonElement.isJsonPrimitive()) {
                                            String enchantString = jsonElement.getAsString();
                                            if (enchantString.contains(":")) {
                                                try {
                                                    String[] splitEnchant = enchantString.split(":");
                                                    esmeta.addStoredEnchant(Enchantment.getByKey(NamespacedKey.minecraft(splitEnchant[0])),
                                                            Integer.parseInt(splitEnchant[1]), true);
                                                } catch (NumberFormatException ex) {
                                                    LunaAuction.LOGGER.warning(String.valueOf(ex));
                                                }
                                            }
                                        }
                                    });
                                }
                            } else if (meta instanceof LeatherArmorMeta) {
                                JsonElement colorElement = extraJson.get("color");
                                if (colorElement != null && colorElement.isJsonPrimitive()) {
                                    LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
                                    try {
                                        leatherArmorMeta.setColor(Color.fromRGB(Integer.parseInt(colorElement.getAsString(), 16)));
                                    } catch (NumberFormatException e) {
                                        LunaAuction.LOGGER.warning(String.valueOf(e));
                                    }
                                }
                            } else if (meta instanceof BookMeta) {
                                JsonElement titleElement = extraJson.get("title");
                                JsonElement authorElement = extraJson.get("author");
                                JsonElement pagesElement = extraJson.get("pages");

                                BookMeta bookMeta = (BookMeta) meta;
                                if (titleElement != null && titleElement.isJsonPrimitive()) {
                                    bookMeta.setTitle(titleElement.getAsString());
                                }
                                if (authorElement != null && authorElement.isJsonPrimitive()) {
                                    bookMeta.setAuthor(authorElement.getAsString());
                                }
                                if (pagesElement != null && pagesElement.isJsonArray()) {
                                    JsonArray jsonArray = pagesElement.getAsJsonArray();
                                    List<String> pages = new ArrayList<>(jsonArray.size());
                                    jsonArray.forEach(jsonElement -> {
                                        if (jsonElement.isJsonPrimitive()) {
                                            pages.add(jsonElement.getAsString());
                                        }
                                    });
                                    bookMeta.setPages(pages);
                                }
                            } else if (meta instanceof PotionMeta) {
                                JsonElement customEffectsElement = extraJson.get("custom-effects");
                                PotionMeta pmeta = (PotionMeta) meta;
                                if (customEffectsElement != null && customEffectsElement.isJsonArray()) {
                                    JsonArray jarray = customEffectsElement.getAsJsonArray();
                                    jarray.forEach(jsonElement -> {
                                        if (jsonElement.isJsonPrimitive()) {
                                            String enchantString = jsonElement.getAsString();
                                            if (enchantString.contains(":")) {
                                                try {
                                                    String[] splitPotions = enchantString.split(":");
                                                    PotionEffectType potionType =
                                                            PotionEffectType.getByName(splitPotions[0]);
                                                    int amplifier = Integer.parseInt(splitPotions[1]);
                                                    int duration = Integer.parseInt(splitPotions[2]) * 20;
                                                    if (potionType != null) {
                                                        pmeta.addCustomEffect(new PotionEffect(potionType, amplifier,
                                                                duration), true);
                                                    }
                                                } catch (NumberFormatException ignored) {
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    JsonObject basePotion = extraJson.getAsJsonObject("base-effect");
                                    PotionType potionType = PotionType.valueOf(basePotion.get("type").getAsString());
                                    boolean isExtended = basePotion.get("isExtended").getAsBoolean();
                                    boolean isUpgraded = basePotion.get("isUpgraded").getAsBoolean();
                                    PotionData potionData = new PotionData(potionType, isExtended, isUpgraded);
                                    pmeta.setBasePotionData(potionData);

                                }
                            } else if (meta instanceof FireworkEffectMeta) {
                                JsonElement effectTypeElement = extraJson.get("type");
                                JsonElement flickerElement = extraJson.get("flicker");
                                JsonElement trailElement = extraJson.get("trail");
                                JsonElement colorsElement = extraJson.get("colors");
                                JsonElement fadeColorsElement = extraJson.get("fade-colors");

                                if (effectTypeElement != null && effectTypeElement.isJsonPrimitive()) {
                                    FireworkEffectMeta femeta = (FireworkEffectMeta) meta;

                                    FireworkEffect.Type effectType = FireworkEffect.Type.valueOf(effectTypeElement.getAsString());

                                    if (effectType != null) {
                                        List<Color> colors = new ArrayList<>();
                                        if (colorsElement != null && colorsElement.isJsonArray())
                                            colorsElement.getAsJsonArray().forEach(colorElement -> {
                                                if (colorElement.isJsonPrimitive())
                                                    colors.add(Color.fromRGB(Integer.parseInt(colorElement.getAsString(), 16)));
                                            });

                                        List<Color> fadeColors = new ArrayList<>();
                                        if (fadeColorsElement != null && fadeColorsElement.isJsonArray())
                                            fadeColorsElement.getAsJsonArray().forEach(colorElement -> {
                                                if (colorElement.isJsonPrimitive())
                                                    fadeColors.add(Color.fromRGB(Integer.parseInt(colorElement.getAsString(), 16)));
                                            });

                                        FireworkEffect.Builder builder = FireworkEffect.builder().with(effectType);

                                        if (flickerElement != null && flickerElement.isJsonPrimitive())
                                            builder.flicker(flickerElement.getAsBoolean());
                                        if (trailElement != null && trailElement.isJsonPrimitive())
                                            builder.trail(trailElement.getAsBoolean());

                                        if (!colors.isEmpty()) builder.withColor(colors);
                                        if (!fadeColors.isEmpty()) builder.withFade(fadeColors);

                                        femeta.setEffect(builder.build());
                                    }
                                }
                            } else if (meta instanceof FireworkMeta) {
                                FireworkMeta fmeta = (FireworkMeta) meta;

                                JsonElement effectArrayElement = extraJson.get("effects");
                                JsonElement powerElement = extraJson.get("power");

                                if (powerElement != null && powerElement.isJsonPrimitive()) {
                                    fmeta.setPower(powerElement.getAsInt());
                                }

                                if (effectArrayElement != null && effectArrayElement.isJsonArray()) {

                                    effectArrayElement.getAsJsonArray().forEach(jsonElement -> {
                                        if (jsonElement.isJsonObject()) {

                                            JsonObject jsonObject = jsonElement.getAsJsonObject();

                                            JsonElement effectTypeElement = jsonObject.get("type");
                                            JsonElement flickerElement = jsonObject.get("flicker");
                                            JsonElement trailElement = jsonObject.get("trail");
                                            JsonElement colorsElement = jsonObject.get("colors");
                                            JsonElement fadeColorsElement = jsonObject.get("fade-colors");

                                            if (effectTypeElement != null && effectTypeElement.isJsonPrimitive()) {

                                                FireworkEffect.Type effectType = FireworkEffect.Type.valueOf(effectTypeElement.getAsString());

                                                if (effectType != null) {
                                                    List<Color> colors = new ArrayList<>();
                                                    if (colorsElement != null && colorsElement.isJsonArray())
                                                        colorsElement.getAsJsonArray().forEach(colorElement -> {
                                                            if (colorElement.isJsonPrimitive())
                                                                colors.add(Color.fromRGB(Integer.parseInt(colorElement.getAsString(), 16)));
                                                        });

                                                    List<Color> fadeColors = new ArrayList<>();
                                                    if (fadeColorsElement != null && fadeColorsElement.isJsonArray())
                                                        fadeColorsElement.getAsJsonArray().forEach(colorElement -> {
                                                            if (colorElement.isJsonPrimitive())
                                                                fadeColors.add(Color.fromRGB(Integer.parseInt(colorElement.getAsString(), 16)));
                                                        });

                                                    FireworkEffect.Builder builder = FireworkEffect.builder().with(effectType);

                                                    if (flickerElement != null && flickerElement.isJsonPrimitive())
                                                        builder.flicker(flickerElement.getAsBoolean());
                                                    if (trailElement != null && trailElement.isJsonPrimitive())
                                                        builder.trail(trailElement.getAsBoolean());

                                                    if (!colors.isEmpty()) builder.withColor(colors);
                                                    if (!fadeColors.isEmpty()) builder.withFade(fadeColors);

                                                    fmeta.addEffect(builder.build());
                                                }
                                            }
                                        }
                                    });
                                }
                            } else if (meta instanceof MapMeta) {
                                MapMeta mmeta = (MapMeta) meta;

                                JsonArray mapView = extraJson.getAsJsonArray("map-view");

                                if(mapView != null && mapView.isJsonArray()) {
                                    JsonArray mapArray = mapView.getAsJsonArray();
                                    if(!(mapArray.isEmpty())) {
                                        LunaAuction.LOGGER.info("MapArray: " + mapArray);
                                        LunaAuction.LOGGER.info("MapArray: " + mapArray.get(0));
                                        LunaAuction.LOGGER.info("MapArray: " + mapArray.get(1));
                                        LunaAuction.LOGGER.info("MapArray: " + mapArray.get(2));
                                        LunaAuction.LOGGER.info("MapArray: " + mapArray.get(3));
                                        LunaAuction.LOGGER.info("MapArray: " + mapArray.get(4));
                                        LunaAuction.LOGGER.info("World: " + mapArray.get(0));

                                        String stringWorld = String.valueOf(mapArray.get(0));
                                        UUID uuid = UUID.fromString(stringWorld.substring(1, stringWorld.length()-1));
                                        //TODO: different map ID
                                        //TODO: player not trackable on map (if map should do it)
                                        try {
                                            MapView view = Bukkit.createMap(Bukkit.getWorld(uuid));
                                            LunaAuction.LOGGER.info("World: " + view.getWorld());
                                            mmeta.setMapId(mapArray.get(1).getAsInt());
                                            MapCanvas canvas;
                                            // mmeta.setColor();
                                            view.setCenterX(Integer.parseInt(String.valueOf(mapArray.get(2))));
                                            view.setCenterZ(Integer.parseInt(String.valueOf(mapArray.get(3))));

                                            //mmeta.getMapView().setCenterX(Integer.parseInt(String.valueOf(mapArray.get(2))));
                                            LunaAuction.LOGGER.info("CenterX: " + view.getCenterX());

                                            //mmeta.getMapView().setCenterZ(Integer.parseInt(String.valueOf(mapArray.get(3))));
                                            LunaAuction.LOGGER.info("CenterZ: " + view.getCenterZ());

                                            if(mapArray.get(4) != null) {
                                                if(mapArray.get(4).getAsString().contains("CLOSEST")) {
                                                    view.setScale(MapView.Scale.CLOSEST);
                                                } else if(mapArray.get(4).getAsString().contains("CLOSE")) {
                                                    view.setScale(MapView.Scale.CLOSE);
                                                } else if(mapArray.get(4).getAsString().contains("NORMAL")) {
                                                    view.setScale(MapView.Scale.NORMAL);
                                                } else if(mapArray.get(4).getAsString().contains("FARTHEST")) {
                                                    view.setScale(MapView.Scale.FARTHEST);
                                                } else if(mapArray.get(4).getAsString().contains("FAR")) {
                                                    view.setScale(MapView.Scale.FAR);
                                                }
                                            }
                                            LunaAuction.LOGGER.info("Scaling: " + view.getScale());

                                            mmeta.setMapView(view);

                                        } catch (Exception e) {
                                            LunaAuction.LOGGER.warning(e + "\n" + Arrays.toString(e.getStackTrace() ));
                                        }
                                    }
                                }
                                JsonElement scalingElement = extraJson.get("scaling");
                                if (scalingElement != null && scalingElement.isJsonPrimitive()) {
                                    mmeta.setScaling(scalingElement.getAsBoolean());
                                }
                                JsonElement locationNameElement = extraJson.get("location-name");
                                if (locationNameElement != null && locationNameElement.isJsonPrimitive()) {
                                    mmeta.setLocationName(locationNameElement.getAsString());
                                }
                                JsonElement colorElement = extraJson.get("color");
                                if (colorElement != null && colorElement.isJsonPrimitive()) {
                                    mmeta.setColor(Color.fromRGB(Integer.parseInt(colorElement.getAsString(), 16)));
                                }
                            }
                        } catch (Exception e) {
                            return null;
                        }
                    }
                    itemStack.setItemMeta(meta);
                    LunaAuction.LOGGER.info("ItemStack: " + itemStack);
                } return itemStack;
            }
        }
        return null;
    }

    static ItemStack getItemInHand(UUID uuid) {
        Player player = Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(String.valueOf(uuid))));
        return player.getInventory().getItemInMainHand();
    }

    static PersistentDataHolder getItemNBTInHand(UUID uuid) {
        Player player = Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(String.valueOf(uuid))));

        ItemStack item = player.getInventory().getItemInMainHand();

        return item.getItemMeta();
    }

    static byte[][] mapToBytes(MapCanvas canvas) {
        byte[][] bytes = new byte[128][128];
        for(int i = 0; i < bytes.length; i++) {
            for(int j = 0; j < bytes.length; j++) {
                bytes[i][j] = canvas.getPixel(i,j);
            }
        }
        return bytes;
    }

    static void mapFromBytes(MapCanvas canvas, byte[][] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < bytes[i].length; j++) {
                canvas.setPixel(i, j, bytes[i][j]);
            }
        }
    }
}
