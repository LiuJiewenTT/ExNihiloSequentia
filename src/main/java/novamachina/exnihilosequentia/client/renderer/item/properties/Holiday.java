package novamachina.exnihilosequentia.client.renderer.item.properties;

// import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;

// This is added when upgrading to 1.21.3+
// public record Holiday() implements SelectItemModelProperty<String> {
//   public static final SelectItemModelProperty.Type<Holiday, String> TYPE =
//       SelectItemModelProperty.Type.create(MapCodec.unit(new Holiday()), Codec.STRING);
//
//   @Nullable
//   @Override
//   public String get(
//       ItemStack stack,
//       @Nullable ClientLevel level,
//       @Nullable LivingEntity entity,
//       int seed,
//       ItemDisplayContext context) {
//     Calendar calendar = Calendar.getInstance();
//     if (calendar.get(Calendar.MONTH) == Calendar.OCTOBER) {
//       return "halloween";
//     } else if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER
//         && calendar.get(Calendar.DAY_OF_MONTH) >= 12
//         && calendar.get(Calendar.DAY_OF_MONTH) <= 26) {
//       return "christmas";
//     }
//
//     return null;
//   }
//
//   @Override
//   public Type<? extends SelectItemModelProperty<String>, String> type() {
//     return TYPE;
//   }
// }
