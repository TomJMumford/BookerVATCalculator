package com.mumford.bookervatcalculator

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ItemList() {
    var itemName by remember { mutableStateOf("") }
    var itemPrice by remember { mutableStateOf("") }
    var itemHasVAT by remember { mutableStateOf(true) }
    var itemsList by remember { mutableStateOf(listOf<Item>()) }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(value = itemName,
                onValueChange = { itemName = it.capitalizeWords() },
                label = { Text("Item name") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            TextField(value = itemPrice,
                onValueChange = { itemPrice = it },
                label = { Text("Item price in GBP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Text("Has VAT?")
                    Switch(checked = itemHasVAT, onCheckedChange = { itemHasVAT = it })
                })

            Button(
                onClick = {
                    if (itemName.isNotEmpty() && itemPrice.isNotEmpty()) {
                        val newItem = Item(itemName, itemPrice.toFloat(), itemHasVAT)
                        itemsList = itemsList.plus(newItem)
                        itemName = ""
                        itemPrice = ""
                        itemHasVAT = true
                    }
                }, modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add Item")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (itemsList.isNotEmpty()) {
                Text(
                    text = "Items List", modifier = Modifier.padding(bottom = 8.dp)
                )

                itemsList.forEachIndexed { index, item ->
                    val itemPriceWithVAT = item.priceWithVAT()
                    val itemPriceString = if (item.hasVAT) "£${"%.2f".format(itemPriceWithVAT)} (incl. VAT)" else "£${"%.2f".format(item.price)} (excl. VAT)"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${index + 1}. ${item.name} - $itemPriceString",
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                itemsList = itemsList.toMutableList().apply { removeAt(index) }
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete item"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                val totalPrice =
                    itemsList.map { if (it.hasVAT) it.priceWithVAT() else it.price }.sum()
                Text(
                    text = "Total Price: £${"%.2f".format(totalPrice)}",
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it ->
    it.lowercase(Locale.ROOT)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
}