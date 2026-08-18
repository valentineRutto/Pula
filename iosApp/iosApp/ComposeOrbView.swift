import SwiftUI
import shared

// SwiftUI wrapper that embeds the Kotlin/Compose UIViewController for the Orb gallery.
// Note: The exact generated Kotlin/Native symbol name for the top-level factory may differ.
// If the call below doesn't compile, open the generated header (shared.framework) to find the
// correct function name — it will be something like `ComValentineruttoOrbmotionIosKt`.

struct ComposeOrbView: UIViewControllerRepresentable {
    var speed: Float = 1.0
    var sizeDp: Float = 48.0
    var themeOrdinal: Int = 1 // 0=Auto,1=Light,2=Dark
    var dotColorArgb: Int64 = 0xFFFFFFFF
    var bgColorArgb: Int64 = 0x00000000

    func makeUIViewController(context: Context) -> UIViewController {
        // Try calling the Kotlin factory. Adjust the symbol name if necessary.
        return ComValentineruttoOrbmotionIosKt.makeThinkingOrbGalleryViewController(
            speed: speed,
            sizeDp: sizeDp,
            themeOrdinal: Int32(themeOrdinal),
            dotColorArgb: dotColorArgb,
            bgColorArgb: bgColorArgb
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // No-op for now. To update params dynamically, provide a Kotlin entry that updates state.
    }
}

struct ComposeOrbView_Previews: PreviewProvider {
    static var previews: some View {
        ComposeOrbView()
    }
}
