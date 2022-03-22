/*
 *  This file is part of the Meteor Tweaks distribution (https://github.com/Declipsonator/Meteor-Tweaks/).
 *  Copyright (c) 2022 Meteor Tweaks.
 *  Licensed Under the GNU Lesser General Public License v3.0
 */

package me.declipsonator.meteortweaks.utils;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public record ClipboardImage(Image image) implements Transferable {

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (isDataFlavorSupported(flavor)) return image;
        throw new UnsupportedFlavorException(flavor);
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor == DataFlavor.imageFlavor;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.imageFlavor};
    }
}