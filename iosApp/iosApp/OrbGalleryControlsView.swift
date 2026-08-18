import SwiftUI

struct OrbGalleryControlsView: View {
    @State private var speed: Float = 1.0
    @State private var sizeDp: Float = 48.0
    @State private var themeIndex: Int = 0
    @State private var dotColorHex: String = "FFFFFFFF"
    @State private var bgColorHex: String = "00000000"

    var body: some View {
        VStack(spacing: 12) {
            Text("Speed: \(String(format: "%.2f", speed))x")
            Slider(value: Binding(get: { Double(speed) }, set: { speed = Float($0) }), in: 0.25...2.0)

            Text("Size: \(Int(sizeDp)) dp")
            Slider(value: Binding(get: { Double(sizeDp) }, set: { sizeDp = Float($0) }), in: 20...128)

            Picker("Theme", selection: $themeIndex) {
                Text("Auto").tag(0)
                Text("Light").tag(1)
                Text("Dark").tag(2)
            }.pickerStyle(SegmentedPickerStyle())

            HStack {
                VStack(alignment: .leading) {
                    Text("Dot color (ARGB hex)")
                    TextField("FFFFFFFF", text: $dotColorHex)
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                }
                VStack(alignment: .leading) {
                    Text("BG color (ARGB hex)")
                    TextField("00000000", text: $bgColorHex)
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                }
            }
            .padding(.horizontal)

            // Compose orb view
            ComposeOrbView(
                speed: speed,
                sizeDp: sizeDp,
                themeOrdinal: themeIndex,
                dotColorArgb: Int64(strtoll(dotColorHex, nil, 16)),
                bgColorArgb: Int64(strtoll(bgColorHex, nil, 16))
            )
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
        .padding()
    }
}

struct OrbGalleryControlsView_Previews: PreviewProvider {
    static var previews: some View {
        OrbGalleryControlsView()
    }
}
