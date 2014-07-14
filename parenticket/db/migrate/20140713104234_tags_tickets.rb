class TagsTickets < ActiveRecord::Migration
  def change
    create_table :tags_tickets, id: false do |t|
      t.integer :ticket_id
      t.integer :tag_id
    end
  end
end
