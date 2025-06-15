package com.example.autotapapp

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class AutoTapService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val root: AccessibilityNodeInfo = rootInActiveWindow ?: return
        traverseAndClick(root)
    }

    private fun traverseAndClick(node: AccessibilityNodeInfo) {
        if (node.text != null) {
            val txt = node.text.toString()
            if (txt.contains("立即开始")) {
                if (node.isClickable) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    return
                } else {
                    var parent = node.parent
                    while (parent != null) {
                        if (parent.isClickable) {
                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                            return
                        }
                        parent = parent.parent
                    }
                }
            }
        }
        for (i in 0 until node.childCount) {
            node.getChild(i)?.let { traverseAndClick(it) }
        }
    }

    override fun onInterrupt() {}
}