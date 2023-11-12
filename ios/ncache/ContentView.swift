//
//  ContentView.swift
//  ncache
//
//  Created by Takenoko on 2023/11/12.
//

import lib
import SwiftUI

struct ContentView: View {
    var body: some View {
        VStack {
            Image(systemName: "globe")
                .imageScale(.large)
            // .foregroundStyle(.tint)
            Text("Hello, world!")
        }
        .padding()
        .onAppear(perform: { Lib() })
    }
}

#Preview {
    ContentView()
}
