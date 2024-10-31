import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public void showAllContacts() {
    Uri uri = ContactsContract.Contacts.CONTENT_URI;
    ArrayList<String> contactList = new ArrayList<>();
    CursorLoader loader = new CursorLoader(this, uri, null, null, null, null);
    Cursor cursor = loader.loadInBackground();

    if (cursor != null) {
        int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

        if (idIndex >= 0 && nameIndex >= 0) { // Kiểm tra chỉ số cột hợp lệ
            while (cursor.moveToNext()) {
                String contact = cursor.getString(idIndex) + " - " + cursor.getString(nameIndex);
                contactList.add(contact);
            }
        }
        cursor.close();
    }

    ListView listView = findViewById(R.id.listView1);
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactList);
    listView.setAdapter(adapter);
}
