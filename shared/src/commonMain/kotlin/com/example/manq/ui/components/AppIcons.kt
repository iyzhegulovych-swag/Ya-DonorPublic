package com.example.manq.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import manq.shared.generated.resources.Res
import manq.shared.generated.resources.ic_add
import manq.shared.generated.resources.ic_arrow_back
import manq.shared.generated.resources.ic_bell
import manq.shared.generated.resources.ic_book
import manq.shared.generated.resources.ic_calendar
import manq.shared.generated.resources.ic_check
import manq.shared.generated.resources.ic_chevron_down
import manq.shared.generated.resources.ic_close
import manq.shared.generated.resources.ic_delete
import manq.shared.generated.resources.ic_edit
import manq.shared.generated.resources.ic_email
import manq.shared.generated.resources.ic_globe
import manq.shared.generated.resources.ic_heart
import manq.shared.generated.resources.ic_heart_outline
import manq.shared.generated.resources.ic_info
import manq.shared.generated.resources.ic_person
import manq.shared.generated.resources.ic_phone
import manq.shared.generated.resources.ic_place
import manq.shared.generated.resources.ic_search
import org.jetbrains.compose.resources.vectorResource

/**
 * Іконки додатку. Контури лежать у composeResources/drawable/ic_*.xml,
 * колір задає Icon через tint.
 */
object AppIcons {

    val Book: ImageVector
        @Composable get() = vectorResource(Res.drawable.ic_book)

    val Place: ImageVector
        @Composable get() = vectorResource(Res.drawable.ic_place)

    val Add: ImageVector
        @Composable get() = vectorResource(Res.drawable.ic_add)

    val Calendar: ImageVector
        @Composable get() = vectorResource(Res.drawable.ic_calendar)

    val Person: ImageVector
        @Composable get() = vectorResource(Res.drawable.ic_person)

    val Search: ImageVector
        @Composable get() = vectorResource(Res.drawable.ic_search)

    val ArrowBack: ImageVector
        @Composable get() = vectorResource(Res.drawable.ic_arrow_back)

    val ChevronDown: ImageVector
        @Composable get() = vectorResource(Res.drawable.ic_chevron_down)

    val Heart: ImageVector
        @Composable get() = vectorResource(Res.drawable.ic_heart)

    val HeartOutline: ImageVector
        @Composable get() = vectorResource(Res.drawable.ic_heart_outline)

    val Phone: ImageVector
        @Composable get() = vectorResource(Res.drawable.ic_phone)

    val Email: ImageVector
        @Composable get() = vectorResource(Res.drawable.ic_email)

    val Globe: ImageVector
        @Composable get() = vectorResource(Res.drawable.ic_globe)

    val Check: ImageVector
        @Composable get() = vectorResource(Res.drawable.ic_check)

    val Bell: ImageVector
        @Composable get() = vectorResource(Res.drawable.ic_bell)

    val Info: ImageVector
        @Composable get() = vectorResource(Res.drawable.ic_info)

    val Edit: ImageVector
        @Composable get() = vectorResource(Res.drawable.ic_edit)

    val Delete: ImageVector
        @Composable get() = vectorResource(Res.drawable.ic_delete)

    val Close: ImageVector
        @Composable get() = vectorResource(Res.drawable.ic_close)
}
