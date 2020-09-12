package com.elf.wartegku.utils.ext

import android.content.Context
import android.widget.Toast

fun Context.toast(m : String) = Toast.makeText(this, m, Toast.LENGTH_SHORT).show()