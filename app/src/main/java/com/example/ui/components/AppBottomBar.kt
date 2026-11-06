package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppStrings
import com.example.model.Language
import com.example.model.NavTab
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.RoyalBlue800

@Composable
fun AppBottomBar(
  currentTab: NavTab,
  currentLanguage: Language,
  isLiveClassActive: Boolean,
  onTabSelected: (NavTab) -> Unit,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "live_pulse")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_alpha"
  )

  NavigationBar(
    modifier = modifier.testTag("app_bottom_bar"),
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 8.dp
  ) {
    // 1. Home Tab
    NavigationBarItem(
      selected = currentTab == NavTab.HOME,
      onClick = { onTabSelected(NavTab.HOME) },
      modifier = Modifier.testTag("nav_tab_home"),
      icon = {
        Icon(
          imageVector = if (currentTab == NavTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
          contentDescription = "Home"
        )
      },
      label = {
        Text(
          text = AppStrings.get("tab_home", currentLanguage),
          fontSize = 11.sp,
          fontWeight = if (currentTab == NavTab.HOME) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = RoyalBlue800,
        selectedTextColor = RoyalBlue800,
        indicatorColor = RoyalBlue800.copy(alpha = 0.12f)
      )
    )

    // 2. Courses Tab
    NavigationBarItem(
      selected = currentTab == NavTab.COURSES,
      onClick = { onTabSelected(NavTab.COURSES) },
      modifier = Modifier.testTag("nav_tab_courses"),
      icon = {
        Icon(
          imageVector = if (currentTab == NavTab.COURSES) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
          contentDescription = "Courses"
        )
      },
      label = {
        Text(
          text = AppStrings.get("tab_courses", currentLanguage),
          fontSize = 11.sp,
          fontWeight = if (currentTab == NavTab.COURSES) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = RoyalBlue800,
        selectedTextColor = RoyalBlue800,
        indicatorColor = RoyalBlue800.copy(alpha = 0.12f)
      )
    )

    // 3. Live Classes Tab
    NavigationBarItem(
      selected = currentTab == NavTab.LIVE,
      onClick = { onTabSelected(NavTab.LIVE) },
      modifier = Modifier.testTag("nav_tab_live"),
      icon = {
        BadgedBox(
          badge = {
            if (isLiveClassActive) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(Color.Red.copy(alpha = pulseAlpha))
              )
            }
          }
        ) {
          Icon(
            imageVector = if (currentTab == NavTab.LIVE) Icons.Filled.LiveTv else Icons.Outlined.LiveTv,
            contentDescription = "Live Classes"
          )
        }
      },
      label = {
        Text(
          text = AppStrings.get("tab_live", currentLanguage),
          fontSize = 11.sp,
          fontWeight = if (currentTab == NavTab.LIVE) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = OrangeAccent,
        selectedTextColor = OrangeAccent,
        indicatorColor = OrangeAccent.copy(alpha = 0.15f)
      )
    )

    // 4. Tests Tab
    NavigationBarItem(
      selected = currentTab == NavTab.TESTS,
      onClick = { onTabSelected(NavTab.TESTS) },
      modifier = Modifier.testTag("nav_tab_tests"),
      icon = {
        Icon(
          imageVector = if (currentTab == NavTab.TESTS) Icons.Filled.AssignmentTurnedIn else Icons.Outlined.Assignment,
          contentDescription = "Tests"
        )
      },
      label = {
        Text(
          text = AppStrings.get("tab_tests", currentLanguage),
          fontSize = 11.sp,
          fontWeight = if (currentTab == NavTab.TESTS) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = RoyalBlue800,
        selectedTextColor = RoyalBlue800,
        indicatorColor = RoyalBlue800.copy(alpha = 0.12f)
      )
    )

    // 5. Profile Tab
    NavigationBarItem(
      selected = currentTab == NavTab.PROFILE,
      onClick = { onTabSelected(NavTab.PROFILE) },
      modifier = Modifier.testTag("nav_tab_profile"),
      icon = {
        Icon(
          imageVector = if (currentTab == NavTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
          contentDescription = "Profile"
        )
      },
      label = {
        Text(
          text = AppStrings.get("tab_profile", currentLanguage),
          fontSize = 11.sp,
          fontWeight = if (currentTab == NavTab.PROFILE) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = RoyalBlue800,
        selectedTextColor = RoyalBlue800,
        indicatorColor = RoyalBlue800.copy(alpha = 0.12f)
      )
    )
  }
}
