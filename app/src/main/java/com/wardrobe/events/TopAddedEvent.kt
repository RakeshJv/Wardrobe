package  com.wardrobe.events

import com.wardrobe.model.TopElement

/**
 * Event to denote the addition of a Top.
 */
data class TopAddedEvent(val topElement: TopElement)
