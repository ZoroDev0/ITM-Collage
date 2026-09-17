import os
import sys
import win32com.client

def convert_docx_to_pdf(docx_path, pdf_path):
    docx_path = os.path.abspath(docx_path)
    pdf_path = os.path.abspath(pdf_path)
    print(f"Converting: {docx_path} -> {pdf_path}")
    
    word = None
    try:
        word = win32com.client.DispatchEx("Word.Application")
        word.Visible = False
        word.DisplayAlerts = 0
        doc = word.Documents.Open(docx_path)
        # 17 = wdFormatPDF
        doc.SaveAs(pdf_path, FileFormat=17)
        doc.Close()
        print("PDF Conversion Complete!")
    except Exception as ex:
        print(f"Error during conversion: {ex}")
        sys.exit(1)
    finally:
        if word is not None:
            word.Quit()

if __name__ == "__main__":
    base_dir = r"d:\Mini-Projects\StudentRecordFileStore"
    docx_file = os.path.join(base_dir, "Student_Record_File_Store_Project_Report_Sasanka_Sekhar_Kundu.docx")
    pdf_file = os.path.join(base_dir, "Student_Record_File_Store_Project_Report_Sasanka_Sekhar_Kundu.pdf")
    convert_docx_to_pdf(docx_file, pdf_file)
